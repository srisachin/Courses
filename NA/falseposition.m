function [ xnext] = falseposition( a,b,tol)
it_count = 0;
xnext=a-(b-a)*f(a)/(f(b)-f(a));
while abs(f(xnext))>tol
    
    iteration = [it_count a b f(xnext)]
    it_count = it_count + 1;
    if f(xnext)*f(b)<0
        a=xnext;
    else
        b=xnext;
    end
    xnext=a-(b-a)*f(a)/(f(b)-f(a));
end
end
function val=f(x)
val=x^2-sin(x)-0.5;
end


