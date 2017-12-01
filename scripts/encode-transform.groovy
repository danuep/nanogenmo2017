/* encode-transform.groovy
 *
 * written in haste, repented at leisure for NaNoGenMo 2017 by github.com/danuep
 */

@Grab(group='de.sciss', module='jwave', version='1.0.3')

def dictionary = [:]
def novelVectors = new File("novel-vectors-word2vec")

// vectors file format is "word x[0] x[1] x[2] .. x[99]"
novelVectors.eachLine { line ->
    line.split(' ').with {
        if (dictionary[it]) { // on encountering a duplicate, prefer the lowercase version
            dictionary[it] = it[1..-1].collect{ it as Double }
        } else {
            def lower = it[0].toLowerCase()
            if (!dictionary[lower]) { // don't over-write the explicit lowercase entry
                dictionary[lower] = it[1..-1].collect { it as Double }
            }
        }
    }
}

def encoded = []

def tokenized = new File('waves-tokens.txt')
tokenized.eachLine { line ->
    if (line) {
        line.split(' ').collect { it.toLowerCase() }.each { wordish ->
            if ( dictionary[wordish] ) {
                encoded << dictionary[wordish]
            } else {
                // for words outside the vector corpus, match greedily within the word
                wordlet = ''
                wordish.each { letter ->
                    if (dictionary[wordlet + letter]) { wordlet = wordlet + letter }
                    else if (wordlet.length() > 1) {
                        encoded << dictionary[wordlet + letter]
                        wordlet = ''
                    }
                }
                if (wordlet) {
                    encoded << dictionary[ '*' ] // gratuitous out-of-vocabulary token
                    println wordlet              // log OOV tokens to stdout
                }
            }
        }
    } else {
        encoded << dictionary[ '---' ] // gratuitous blank line token
    }
}

// transpose vectors into streams by dimension and load as double[] for JWave
def streams = []
encoded[0].each { streams << new double[encoded.size()] }
encoded.eachWithIndex { entry, index ->
    entry.eachWithIndex { value, column ->
        streams[column][index] = value as double
    }
}

def t = new jwave.Transform(
               new jwave.transforms.AncientEgyptianDecomposition( // for arbitrary-length sequences
                new jwave.transforms.FastWaveletTransform(
                 new jwave.transforms.wavelets.daubechies.Daubechies8( ) ) ) )
def transformed = streams.collect {
    t.forward( it )
}

// transpose back -- can't use Groovy transpose() because of type mix?
def output = []
transformed.eachWithIndex { dimension, column ->
    dimension.eachWithIndex { value, index ->
        if (column==0) { output[index] = [value] }
        else { output[index] << value }
    }
}

def out = new File('waves-transformed.txt')
out.withWriter { writer ->
    output.each { writer.println it.join(' ') }
}

