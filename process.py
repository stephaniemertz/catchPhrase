
# coding: utf-8

# In[2]:

global stopwords
stopwords= ['i','a','about','an','are','as','at','be','by','com','for','from','how','in','is','it','of','on','or','that','the','this','to','was','what','when','where','who','will','with']


# In[3]:

def tokenize(text, lowercase=True):
    """
    Args:
    text: text to tokenize
    lowercase: boolean indicating whether string should be lowercased

    Return:
    A list of the words tokenized after splitting on spaces and removing punctuation 
    """

    # Lowercase string if the flag is set
    if lowercase:
        text = text.lower()

    # tokenize and remove punctuation marks from tokens
    tokenized = text.split()
    tokenized = [word.strip('.,?!;:-\\\'/()""') for word in tokenized]
    
    return tokenize


# In[4]:

def drop_tokens(tokenized, usestopwords=True):
    """
    Args:
    tokenized: list of strings
    stopwords: boolean

    Returns:
    A list of strings 

    Removes stop words from tokenized. 
    """
    global stopwords
    # If stopwords is set to True, use default stopwords list
    if usestopwords == True:
        usestopwords = stopwords
    # If set to False, then it's an empty list
    elif stopwords == False:
        stopwords = []
    
    # Drop words in td
    processed_text = [n for n in tokenized if n not in stopwords]

    return processed_text

# In[5]:

def vectorize(text, to_count): 
    """""
    Args:
    text: tokenized text
    to_count: list of strings,  filler words

    Returns:
    A list of floats representing relative frequencies of to_count in text
    Good for graphing if we're doing a web app
    """""
    vectors = [0.0] * len(to_count) 
    for token in text: 
        if token in to_count:
            column = to_count.index(token)
            vectors[column] += 1 

   	#turns the tokens into a float and divides that by total text to find frequency
    ntokens = float(len(text))
    vectors = [v/ntokens for v in vectors] 
              
    return vectors 


# In[6]:

def word_count(tokens, wd_freqs={}):

    """
    Args:
        tokens: list of strings
    wd_freqs: dictionary of {token: frequency} counts

    Returns:
    Dictionary of token frequency counts
    """

    for t in tokens:
        if t in wd_freqs:
            wd_freqs[t]+=1
        else:
            wd_freqs[t]=1

    return wd_freqs


# In[ ]:




# In[ ]:




# In[ ]:



