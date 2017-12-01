/* tokenize.groovy
 *
 * written in haste, repented at leisure for NaNoGenMo 2017 by github.com/danuep
 */

def basetext = new File('waves.txt')
def tokenized = new File('waves-tokens.txt')
tokenized.withWriter('utf-8') { writer ->
    basetext.eachLine { line ->
        println line
        def words = line.split('\\s').collect { wordish ->
            // collect consecutive alpha characters as a token, while splitting all others
            // yes I'm sure there's a better way to do this
            wordish.inject([''], { acc, c ->
                if ( Character.isLetter(c as char) ) {
                    acc[-1] = acc[-1] + c
                } else {
                    acc << c << ''
                }
                acc
            }).findAll { it }
        }.flatten().join(' ').replaceAll('"', "''") // word vector file doesn't contain `"`
        println words
        writer.writeLine words
    }
}

