function average = averageEpisodeLengths(fileName, rangeMin, rangeMax)

rangeMinStr = num2str(rangeMin);
sum = importdata(strcat(fileName, rangeMinStr, '.txt'));
for i=rangeMin+1:rangeMax
   iStr = num2str(i);
   sum = sum + importdata(strcat(fileName, iStr, '.txt')); 
end

average = sum/(rangeMax-rangeMin+1);