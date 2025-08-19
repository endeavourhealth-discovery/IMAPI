package org.endeavourhealth.imapi.logic.reasoner;

import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.tartarus.snowball.ext.PorterStemmer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TextMatcher {

  private static final double FUZZY_THRESHOLD = 0.85; // Tweak as needed

  private static final JaroWinklerDistance jw = new JaroWinklerDistance();


  private static List<String> tokenizeAndStem(String text) {
    PorterStemmer stemmer = new PorterStemmer();
    return Arrays.stream(text.toLowerCase().split("\\s+"))
      .map(w -> {
        stemmer.setCurrent(w);
        stemmer.stem();
        return stemmer.getCurrent();
      })
      .collect(Collectors.toList());
  }


  private static double jaccardSimilarity(Set<String> a, Set<String> b) {
    Set<String> intersection = new HashSet<>(a); intersection.retainAll(b);
    Set<String> union = new HashSet<>(a); union.addAll(b);
    return (double) intersection.size() / union.size();
  }




private static  double avgBestMatch(List<String> wordsA, List<String> wordsB) {
    JaroWinklerDistance jwd = new JaroWinklerDistance();
    double sum = 0;
    for (String wordA : wordsA) {
      double best = 0;
      for (String wordB : wordsB) {
        double score = jwd.apply(wordA, wordB);
        if (score > best) best = score;
      }
      sum += best;
    }
    return sum / wordsA.size(); // or size of the larger list
  }

  private static double hasPrefixMatch(String searchToken,List<String> synonymTokens) {
    int score=10;
    for (int i=0;i<synonymTokens.size();i++){
      if (synonymTokens.get(i).startsWith(searchToken)) return score;
      else score--;
    }
   return 0;
  }






  public static boolean matchTerm(String searchTerm, String synonym) {
    synonym=synonym.split(" \\(")[0];
    List<String> searchTokens = tokenizeAndStem(searchTerm);
    List<String> synonymTokens = tokenizeAndStem(synonym);
    JaroWinklerSimilarity jw = new JaroWinklerSimilarity();

    double jaroScore = jw.apply(String.join(",",searchTokens), (String.join(",",synonymTokens)));
    double jaccardScore = jaccardSimilarity(new HashSet<>(searchTokens), new HashSet<>(synonymTokens));
    double prefixBonus = hasPrefixMatch(searchTokens.get(0),synonymTokens);

    double hybridScore = 0.5 * jaroScore + 0.5 * jaccardScore + prefixBonus;
    return hybridScore >= FUZZY_THRESHOLD;
  }
}

