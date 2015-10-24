from nltk.corpus import stopwords

def tokenize(sentence, lowercase=True):

	Args:
	sentence: string to tokenize
	lowercase: boolean indicating whether string should be lowercased

	Returns:
	A list of string
	
	Breaks up string by spaces into tokens
   	Removes punctuation from those tokens


	# Lowercase string if the flag is set
	if lowercase:
		sentence = sentence.lower()
	
	# tokenize and strip punctuation marks from tokens
	tokenized = sentence.split()
	tokenized = [word.strip('.,?!;:-\\\'/()""') for word in tokenized]
	
	return tokenized

def drop_tokens(tokenized, to_drop, stopwords=True):

	Args:
	tokenized: list of strings
	to_drop: list of strings 
	stopwords: boolean

	Returns:
	A list of strings 

	Removes tokens in to_drop from tokenized. Also gives option to remove stopwords
	Converts the to_drop list to lowercase


	# Lowercase to_drop
	td = [n.lower() for n in to_drop]

	# If stopwords is simply set to True, use default stopwords list
	if stopwords == True:
		stopwords = stopwords.words('english')
	# If set to False, then it's an empty list
	elif stopwords == False:
		stopwords = []

	# Otherwise we know stopwords is a list of strings
	# Add it to td
	td += stopwords
	
	# Drop words in td
	processed_text = [n for n in tokenized if n not in td]

	return processed_text
	
def vectorize(text, to_count): 

	Args:
	text: list of strings
	to_count: list of strings

	Returns:
	A list of floats representing relative frequencies of to_count in text

	vectors = [0.0] * len(to_count) 
	for token in text: 
		if token in to_count:
			column = to_count.index(token)
			vectors[column] += 1 

   	#turns the tokens into a float and divides that by total text to find frequency
	ntokens = float(len(text))
	vectors = [v/ntokens for v in vectors] 
			  
	return vectors 

def word_count(tokens, wd_freqs={}):

	Args:
	tokens: list of strings
	wd_freqs: dictionary of {token: frequency} counts

	Returns:
	Dictionary of token frequency counts


	for t in tokens:
		if t in wd_freqs:
			wd_freqs[t]+=1
		else:
			wd_freqs[t]=1

	return wd_freqs
