# vecs2text.py
# shamelessly cribbed from similarity.py at https://github.com/aparrish/phonetic-similarity-vectors
# written in haste, repented at leisure for NaNoGenMo 2017 by github.com/danuep
import sys
from annoy import AnnoyIndex

t = AnnoyIndex(100, metric='angular')
words = list()
lookup = dict()

print("loading...", file=sys.stderr)
for i, line in enumerate(open("novel-vectors-word2vec", encoding="latin1")):
    line = line.strip()
    tokens = line.split(" ")
    # vector file format is "word x[0] x[1] x[2] .. x[99]"
    word = tokens[0]
    vec_s = tokens[1:]
    vec = [float(n) for n in vec_s]
    if len(vec)==100:
        t.add_item(i, vec)
        lookup[word] = vec
        words.append(word)
print("building index...", file=sys.stderr)
t.build(100)
print("done.", file=sys.stderr)

print("writing resolved text to file", file=sys.stderr)
wavelets = open('the-wavelets.txt','w')
for i, vec_s in enumerate(open("waves-transformed.txt", encoding="latin1")):
    vec_s = vec_s.split(" ")
    vec = [float(n) for n in vec_s]
    try:
        wavelets.write( words[t.get_nns_by_vector(vec, 1)[0]] + ("\n" if i%10==9 else " ") )
    except KeyError:
        print("not found")
wavelets.close()
print("done writing files.", file=sys.stderr)
