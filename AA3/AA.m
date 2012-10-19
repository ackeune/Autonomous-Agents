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
for preds = 1:2
    plotAA(name, preds, alpha, gamma, epsilon, episodes, runs, 'DiagState', saveDir);
end