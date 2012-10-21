function AA
close all;

% varying predator amount
name = 'IQL';
alpha = 0.5;
gamma = 0.9;
epsilon = 0.1;
episodes = 500;
runs = 100;
saveDir = '../AA3PDFs/';
if false %for preds=1:2
    plotAA(name, preds, alpha, gamma, epsilon, episodes, runs, 'DiagState', saveDir);
    plotAA(name, preds, alpha, gamma, epsilon, episodes, runs, 'RelativeState', saveDir);
end

episodes = 1000;
if false %for preds=1:3
    plotAA(name, preds, alpha, gamma, epsilon, episodes, runs, 'DiagState', saveDir);
end

name = 'HMQL';
if false %for preds = 1:2
    plotAA(name, preds, alpha, gamma, epsilon, episodes, runs, 'DiagState', saveDir);
end


%plotAA('IQL', 2, 0.5, 0.9, 0.1, 10000, 10, 'RelativeState', '../AA3PDFs/');
%plotAA('IQL', 2, 0.5, 0.9, 0.1, 10000, 10, 'DiagState', '../AA3PDFs/');
%plotAA('HMQL', 2, 0.5, 0.9, 0.1, 10000, 10, 'DiagState', '../AA3PDFs/');
%plotAA('IQL',3,0.5,0.9,0.1,10000,100,'DiagState','../AA3PDFs/');
%%%%%plotAA('IQL',3,0.5,0.9,0.1,50000,10,'DiagState','../AA3PDFs/');