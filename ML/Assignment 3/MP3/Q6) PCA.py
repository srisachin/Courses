from sklearn import decomposition
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.colors import ListedColormap
from sklearn import neighbors, datasets
from sklearn.cross_validation import StratifiedKFold
from sklearn.naive_bayes import GaussianNB
import pylab as pl
import scipy.io as sio
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
from sklearn.lda import LDA

d = np.genfromtxt ('/home/sachin/Downloads/document.csv', delimiter=",")
d = np.array(d)
#print(d)
pca = decomposition.PCA(n_components=3)
pca.fit(d)
X = pca.transform(d)

ppca = decomposition.ProbabilisticPCA(n_components=3)
ppca.fit(d)
PX = ppca.transform(d)

fig1 = plt.figure()
plt.scatter(X[:, 0], X[:, 2], c='r')
#pl.show()

for i in range(0,9):
    plt.annotate(i+1, (X[i, 0],X[i, 2]))

fig2 = plt.figure()
plt.scatter(PX[:, 0], PX[:, 2], c='y')

for i in range(0,9):
    plt.annotate(i+1, (PX[i, 0],PX[i, 2]))

d1 = np.genfromtxt ('/home/sachin/Downloads/document1.csv', delimiter=",")

pca.fit(d1)
X1 = pca.transform(d1)

fig3 = plt.figure()
plt.scatter(X1[:, 0], X1[:, 2], c='b')

for i in range(0,10):
    plt.annotate(i+1, (X1[i, 0],X1[i, 2]))

clf = LDA()
y=[1,0,0,0,0,0,0,0,0,0]
clf.fit(X1, y)
LX = clf.transform(X1)
fig4 = plt.figure()
plt.scatter(LX,[0,0,0,0,0,0,0,0,0,0])
for i in range(0,10):
    plt.annotate(i+1, (LX[i],0))
pl.show()
