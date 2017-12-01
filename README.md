# Scripts to generate THE WAVELETS

### A National Novel Generation Month 2017 entry

The idea of this project was to take the word-vector-space projection of a novel and perform a wavelet transform on it. The results of wavelet transforms on images can produce a sort of ghostly expansion, and I thought it would be worth seeing what that might look like on text.

## Dependencies

* groovy
  * `encode-transform.groovy` will grab `de.sciss:jwave`
* python 3
  * annoy
* full text of *The Waves* by Virginia Woolf, currently available at https://ebooks.adelaide.edu.au/w/woolf/virginia/w91w/
* word vectors generated from Project Gutenberg novels by @aparrish, currently available at https://s3.amazonaws.com/aparrish/novel-vectors-word2vec.gz

## Steps

1. Preprocess
   * Drop the text in the same directory as the scripts as `waves.txt`.
   * The text of *The Waves* at the above link has special characters (smart quotes and em dashes) which are not represented in `novel-vectors-word2vec`. I did a lazy search-and-replace to convert those to `'`, `''`, and `--`.
1. `groovy tokenize.groovy`
1. `groovy encode-transform.groovy`
1. `python3 vecs2text.py`

Output will be in `the-wavelets.txt`.

## Results

* [The Wavelets](https://gist.github.com/danuep/8ba4e4d51293b4f33d4289ceb5fcb97e)
* Bonus: [Haar of Darkness](https://gist.github.com/danuep/46d3c76be8f42685b7f77e76c57343fa) (Haar wavelet transform of Conrad's *Heart of Darkness*)

The results are slightly entertaining, but iffy as a legible text. It's reassuring to see that the average value for both texts (the first word in the transformed text) roughly aligns with the mood of the text. Subsequent words represent the differences between sections of text, which I think is harder to intuit than pixel values representing differences in sections of an image. (Specifically, the second-through-fourth words of *The Wavelets* tells us that the second half of *The Waves* is more `Grandmother` than the first, the second quarter is more `Mas'r` than the first, and the last quarter more `Meadows` than the third).

## Future work

* Accept command line arguments for file names
* Smarter tokenization
* Low-pass filtered output for "summarization"
* Merging two works by averaging in the frequency domain and wavelet reconstruction
