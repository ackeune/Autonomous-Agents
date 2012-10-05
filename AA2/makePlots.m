function makePlots


if true
eGreedyFixedE1I15 = averageEpisodeLengths('episodeLengths_egreedy', 0, 9);
eGreedyFixedE1I15Subset = eGreedyFixedE1I15([1, 4, end-3, end], :);
h = figure;
plot(eGreedyFixedE1I15Subset');
legend('a=0.1, g=0.1', 'a=0.1, g=0.9', 'a=0.5, g=0.1', 'a=0.5, g=0.9');
xlabel('episode');
ylabel('episode length');
saveas(h, '../eGreedyFixedE1I15Extremes', 'pdf');



sarsaE1I15 = averageEpisodeLengths('episodeLengths_sarsa', 0, 9);
sarsaSubset = sarsaE1I15([1, 4, end-3, end], :);
h = figure;
plot(sarsaSubset');
legend('a=0.1, g=0.1', 'a=0.1, g=0.9', 'a=0.5, g=0.1', 'a=0.5, g=0.9');
xlabel('episode');
ylabel('episode length');
saveas(h, '../sarsaFixedE1I15Extremes', 'pdf');

qLearningVsSarsa = [eGreedyFixedE1I15(end, :); sarsaE1I15(end,:)];
h = figure;
plot(qLearningVsSarsa');
legend('q-Learning', 'sarsa');
xlabel('episode');
ylabel('episode length');
saveas(h, '../qLearningVsSarsa', 'pdf');

qLearningA5G9 = averageEpisodeLengths('episodeLengths_egreedy_fixedAlpha0,500000Gamma0,900000_', 0, 9);
qInitialValues = qLearningA5G9(1:4, :);
h = figure;
plot(qInitialValues');
legend('I=5', 'I=10', 'I=15', 'I=30');
xlabel('episode');
ylabel('episode length');
saveas(h, '../qLearningA5G9E1', 'pdf');

qEpsilon = qLearningA5G9([3,7,11,15], :);
h = figure;
plot(qEpsilon');
legend('e=0.1', 'e=0.3', 'e=0.5', 'e=0.7');
xlabel('episode');
ylabel('episode length');
saveas(h, '../qLearningA5G9I15', 'pdf');

end

% ex1
if true
eGreedyFixedE1I15 = averageEpisodeLengths('episodeLengths_egreedy', 0, 9);
eGreedyG1 = eGreedyFixedE1I15([1, 5, 9, 13, 17], :);
h = figure;
plot(eGreedyG1');
legend('a=0.1', 'a=0.2', 'a=0.3', 'a=0.4', 'a=0.5');
xlabel('episode');
ylabel('episode length');
title('g=0.1')
saveas(h, '../eGreedyFixedG1', 'pdf');

eGreedyG5 = eGreedyFixedE1I15([2, 6, 10, 14, 18], :);
h = figure;
plot(eGreedyG5');
legend('a=0.1', 'a=0.2', 'a=0.3', 'a=0.4', 'a=0.5');
xlabel('episode');
ylabel('episode length');
title('g=0.5')
saveas(h, '../eGreedyFixedG5', 'pdf');

eGreedyG7 = eGreedyFixedE1I15([3, 7, 11, 15, 19], :);
h = figure;
plot(eGreedyG7');
legend('a=0.1', 'a=0.2', 'a=0.3', 'a=0.4', 'a=0.5');
xlabel('episode');
ylabel('episode length');
title('g=0.7')
saveas(h, '../eGreedyFixedG7', 'pdf');

eGreedyG9 = eGreedyFixedE1I15([4, 7, 12, 16, 20], :);
h = figure;
plot(eGreedyG9');
legend('a=0.1', 'a=0.2', 'a=0.3', 'a=0.4', 'a=0.5');
xlabel('episode');
ylabel('episode length');
title('g=0.9')
saveas(h, '../eGreedyFixedG9', 'pdf');
end

if true
eGreedyFixedE1I15 = averageEpisodeLengths('episodeLengths_egreedy', 0, 9);
softmaxFixedT1I15 = averageEpisodeLengths('episodeLengths_softmax', 0, 0);
subset = [eGreedyFixedE1I15(end,:); softmaxFixedT1I15(end, :)];
h = figure;
plot(subset');
legend('e-greedy', 'softmax');
xlabel('episode');
ylabel('episode length');
title('e=0.1, t=0.1')
saveas(h, '../eGreedyVsSoftmax', 'pdf');

%plot(eGreedyFixedE1I15(end, :));

% softmaxFixedA5G9 = averageEpisodeLengths('softmaxFixedA5G9', 0, 9);
% softmaxSubset = softmaxFixedA5G9(1, :);
% h = figure;
% plot(softmaxSubset')

end

if true
% compare e-greedy and softmax
eGreedySoftmax = averageEpisodeLengths('compareEGreedySoftmaxFixedA5G9_', 0, 9);
eGreedy = eGreedySoftmax([1, 3, 5, 7], :);
softmax = eGreedySoftmax([2, 4, 6, 8], :);
h = figure;
plot(eGreedy');
legend('e=0.1', 'e=0.3', 'e=0.5', 'e=0.7');
xlabel('episode');
ylabel('episode length');
title('e-greedy');
saveas(h, '../eGreedyMixedEpsilon', 'pdf');

h = figure;
plot(softmax');
legend('t=0.1', 't=0.3', 't=0.5', 't=0.7');
xlabel('episode');
ylabel('episode length');
title('softmax');
saveas(h, '../softmaxMixedTemperature', 'pdf');

h = figure;
compare = [eGreedy(1,:); eGreedy(end,:) ; softmax(1,:); softmax(end,:)];
plot(compare')
legend('e-greedy, e=0.1', 'e-greedy, e=0.7', 'softmax, t=0.1', 'softmax, t=0.7');
xlabel('episode');
ylabel('episode length');
saveas(h, '../compareEGreedySoftmax', 'pdf');
end

if true
qLearningVsSarsa = averageEpisodeLengths('qLearningVsSarsa', 0, 9);
h = figure;
plot(qLearningVsSarsa');
legend('q-learning', 'sarsa');
xlabel('episode');
ylabel('episode length');
saveas(h, '../compareQLearningSarsa', 'pdf');
end
