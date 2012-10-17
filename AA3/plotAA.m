function plotAA
close all;
% 1 predator
fileName = 'IQL_Preds1Alpha0,5Gamma0,9Epsilon0,1';
data = importdata(strcat(fileName, '.txt'));
h = figure(1);
plot(data(1,:));
title('1 predator');
xlabel('Episode');
ylabel('Episode length');
saveas(h, strcat('../AA3PDFs/', fileName), 'pdf');
% predators>1
for i=2:2
    fileName = strcat('IQL_Preds', num2str(i), 'Alpha0,5Gamma0,9Epsilon0,1');
    data = importdata(strcat(fileName, '.txt'));
    %data = data(:, length(data)-100:end);
    h = figure(i);
    subplot(2,1,1)
    plot(data(1,:));
    title(strcat(num2str(i), ' predator'));
    xlabel('Episode');
    ylabel('Episode length');
    subplot(2,1,2);
    plot(data(2,:));
    xlabel('Episode');
    ylabel('Prey caught ratio');
    saveas(h, strcat('../AA3PDFs/', fileName), 'pdf');
end
