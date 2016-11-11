# coding=utf-8;

import pymorphy2
from gensim.models import word2vec
from numpy.random import choice
import matplotlib.pyplot as plt
from scipy.stats import ttest_ind,mannwhitneyu,wilcoxon
import config

class SentenceDetector(object):
    
    def __init__(self):
        self.model = word2vec.Word2Vec.load_word2vec_format(config.corpuses[0], binary=True)
        self.morph = pymorphy2.MorphAnalyzer()
        self.endings = {'NOUN':'_S', 'VERB':'_V', 'ADJF':'_A', 'ADJS':'_A', 'ADVB':'_ADV'}

    def rusvec_form(self, word):
        parsed = self.morph.parse(word)[0]
        if parsed.tag.POS not in self.endings:
            return word
        norm = parsed.normal_form + self.endings[parsed.tag.POS]
        return norm

    def detect(self, data, value, verbose = 'some', alpha = 0.025):
        rus_data = {" ".join(self.rusvec_form(d) for d in x.split() if self.rusvec_form(d) in self.model) for x in data}-{''}
        value_data = " ".join(self.rusvec_form(d) for d in value.split() if self.rusvec_form(d) in self.model)

        if not value_data:
            return False

        all_rus_data_list = [[self.model.n_similarity(x.split(), d.split()) for d in rus_data-{x}] for x in rus_data]
        all_rus_data = [x for sublist in all_rus_data_list for x in sublist]
        vec_value_data = [self.model.n_similarity(value_data.split(), d.split()) for d in rus_data]

        if verbose == 'full':
            print(rus_data)
            print(value_data)
            print(all_rus_data)
            print(vec_value_data)
            plt.hist(all_rus_data, len(vec_value_data), normed=1, facecolor='g', alpha=0.45)
            plt.hist(vec_value_data, len(vec_value_data), normed=1, facecolor='r', alpha=0.45)
            plt.title(value)
            plt.show()
            plt.close()
            plt.title(value)
            plt.boxplot([all_rus_data, vec_value_data], whis=1)
            plt.show()

            print("TTEST-Different: t=%f p=%f" % ttest_ind(all_rus_data,vec_value_data, equal_var=False))
            print("MWU-Different: u=%f p=%f" % mannwhitneyu(all_rus_data,vec_value_data, alternative='two-sided'))
            print("MWU-Same: u=%f p=%f" % mannwhitneyu(all_rus_data,all_rus_data, alternative='two-sided'))

        uvalue, pvalue = mannwhitneyu(all_rus_data,vec_value_data, alternative='two-sided')
        addition = "" if pvalue < alpha else "     OK!"
        if verbose == 'some':
            print("MWU-Different: u={} p={:f} {}".format(uvalue, pvalue, addition))

        return pvalue > alpha
