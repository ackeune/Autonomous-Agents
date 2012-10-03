function makePlots

eGreedyFixedE1I15 = averageEpisodeLengths('episodeLengths_egreedy', 0, 9);
eGreedyFixedE1I15Subset = eGreedyFixedE1I15([1, 4, end-3, end], :);
h = figure;
plot(eGreedyFixedE1I15Subset');
legend('a=0.1, g=0.1', 'a=0.1, g=0.9', 'a=0.5, g=0.1', 'a=0.5, g=0.9');
xlabel('episode');
ylabel('episode length');
saveas(h, 'eGreedyFixedE1I15Extremes', 'pdf');
