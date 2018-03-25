clc
clear all 
close all

N = 700 ;
X = 0:1:N ;

f= @(n) exp(-(0.4)^3.*n.^(1.4)) ;
f(300)

figure (1)
plot (X,f(X))
grid on ;

% f= @(n) exp(-n./(N-n+N/5)) ;
% figure (2) ;
% plot (X,f(X))
% grid on ;

