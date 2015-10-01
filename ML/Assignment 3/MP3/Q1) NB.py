
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.colors import ListedColormap
from sklearn import neighbors, datasets
from sklearn.cross_validation import StratifiedKFold
from sklearn.naive_bayes import GaussianNB

iris = datasets.load_iris()

# import some data to play with
iris = datasets.load_iris()
X = iris.data
y = iris.target


skf = StratifiedKFold(y, 5)
f = [0]*150
for train, test in skf:
	#print(train, test)
	#for weights in ['uniform', 'distance']:
		#clf = neighbors.KNeighborsClassifier(n_neighbors, weights=weights)
	gnb = GaussianNB()
	gnb.fit(X[train], y[train])

#	for i in test:	
#		f[i] = f[i] + clf.predict(X[i])
#		print clf.predict(X[i])
	f=f+gnb.predict(X)

f[:] = [x / 5 for x in f]	
print f
b=0.
for i in range(0,150):
	b = b+(f[i]-y[i])**2 
b=b/150
print("Bias")
print(b)
v= gnb.predict(X) - f
var=0.
for i in range(0,150):
	var = var+v[i]**2
var=var/(150*5)
print("Variance")
print(var)

