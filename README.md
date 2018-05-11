# Wireless_Security_Pattern_Lock
A probabilistic (predictive) model using Machine Learning, that guesses the pattern lock of a user.

-Gesture-based unlock pattern: It is still the primary means of first-step user authentication in Android devices.

-Assumption: Users tend to choose and repeat memorable unlock patterns.

-Challenge: Find a way to break or circumvent the unlocking mechanism of such devices.

-Project's goal: Investigate whether it is possible to guess the correct unlock pattern provided that we have only partial knowloedge of it, say, we know the first 3 moves.

-Example: When the user is at position 7 the probability of choosing 1,2,3,6,9 as their next move is 0. The probability of choosing 4 again is very low. Therefore, only position 5 and 8 gather significant chances of being selected as next moves.

-Questions: Which one of these choices gathers greater chances of being selected? Is it possible to predict a user's next n moves if k moves are known in advnace ? What is the minimum amount of known moves for which the predictive model is effective?
