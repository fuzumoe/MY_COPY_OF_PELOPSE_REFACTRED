% function p = point(a,b,c)
function [x,y,z] = point (Ex,Ey,Ez,Vx,Vy,Vz,sampleNumber)
% R = normrnd( mean, standard deviation, m, n)
% generates an m-by-n-by-... array R
% n = sample number

x = normrnd(Ex, Vx,1,sampleNumber); 
y = normrnd(Ey, Vy,1,sampleNumber); 
z = normrnd(Ez, Vz,1,sampleNumber); 
%y = b+randn(1,d);
%z = c+randn(1,d);
p = transpose([x; y; z]);
%y = a*x+b;
%figure 
%scatter3(x,y,z)