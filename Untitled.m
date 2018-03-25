clc
clear all 
close all

N = 10000 ;
iterations = 0:1:N 

f= @(n) exp(-n/(N-n)) ;
y=f(iterations) 

plot (iterations,y)