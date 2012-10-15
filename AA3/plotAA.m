function plotAA
close all;

fileName = 'IQL_Preds1Alpha0,5Gamma0,9Epsilon0,1';
data = importdata(strcat(fileName, '.txt'));
h = figure(1);
plot(data(1,:));
title('1 predator');
xlabel('Episode');
ylabel('Episode length');
saveas(h, strcat('../AA3PDFs/', fileName), 'pdf');

% subplot(2,1,1)
% plot(data(1,:));
% subplot(2,1,2)
% plot(data(2,:));
