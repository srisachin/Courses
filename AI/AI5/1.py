import math
   
def NN(examples):
    w0 = 0.2
    w1 = 1
    w2 = -1
    n= 1
    flag=0
    
    for k in range(0,20):
        flag=0
        for i in examples:
            inputs = w0 + w1 * i[0] + w2 * i[1]
            if(i[2]*inputs <= 0):
                w0 = w0 + n * i[2] * 1 
                w1 = w1 + n * i[2] * i[0] 
                w2 = w2 + n * i[2] * i[1]
                flag=1
        print( w0, w1, w2, k, sep=' ' )
        if(flag==0):
            break

            
    
def main():
    examples = [
                [.1, .7, 1],
                [.1, 1, -1],
                [.3, .6, 1],
                [.35, 1, -1],
                [.45, .1, 1],
                [.6, .3, 1],
                [.7, .65, -1],
                [.95, .4, -1],
                ]
    NN(examples)


if __name__ == '__main__':
    main()
