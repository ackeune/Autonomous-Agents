function makePlots

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




