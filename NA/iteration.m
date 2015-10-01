function root = iteration(x0,error_bd,max_iterate,index_f)


format short e
error = 1;
it_count = 0;
while abs(error) > error_bd && it_count <= max_iterate
    gx = g(x0,index_f);
    
  
    x1 = gx;
    error = x1 - x0;
%   Internal print of newton method. Tap the carriage
%   return key to continue the computation.
    iteration = [it_count x0 error]
    pause
    x0 = x1;
    it_count = it_count + 1;
end

if it_count > max_iterate
    disp('The number of iterates calculated exceeded') 
    disp('max_iterate.  An accurate root was not')
    disp('calculated.')
else
    format long
    root = x1
    format short e
    error
    format short
    it_count
end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%
function value = g(x,index)

% function to define equation for rootfinding problem.

switch index
case 1        
    value = 1+.5*sin(x);
case 2
    value = 3+2*sin(x);
   
end

