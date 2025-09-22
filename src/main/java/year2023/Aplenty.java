package year2023;

import base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.*;

public class Aplenty extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResourceFile(2023, 19, false, 0);
        Set<Workflow> workflows = new HashSet<>();
        Set<Map<Character, Integer>> parts = new HashSet<>();
        int i = 0;
        while (i < lines.size()) {
            String line = lines.get(i++);
            if (line.isBlank()) break;
            workflows.add(new Workflow(
                    line.substring(0, line.indexOf('{')),
                    Arrays.stream(line.substring(line.indexOf('{') + 1, line.length() - 1).split(",")).map(Rule::new).toList()
            ));
        }
        for (int j = i; j < lines.size(); j++) {
            String[] properties = lines.get(j).substring(1, lines.get(j).length() - 1).split(",");
            Map<Character, Integer> part = new HashMap<>();
            for (String property : properties) {
                part.put(property.charAt(0), Integer.parseInt(property.substring(2)));
            }
            parts.add(part);
        }
        Workflow startingWorkflow = workflows.stream().filter(w -> w.name().equals("in")).findAny().orElseThrow();
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = addRatings(parts, startingWorkflow, workflows);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = findValidCombos(startingWorkflow, workflows);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private long findValidCombos(Workflow startingWorkflow, Set<Workflow> workflows) {
        List<Map<Character, Pair<Integer, Integer>>> unexploredSegments = new ArrayList<>();
        long runningTotal = 0L;
        Map<Character, Pair<Integer, Integer>> initialMap = new HashMap<>();
        for (char c : "xmas".toCharArray()) {
            initialMap.put(c, Pair.of(1, 4001));
        }
        unexploredSegments.add(initialMap);
        while (!unexploredSegments.isEmpty()) {
            Map<Character, Pair<Integer, Integer>> segment = unexploredSegments.remove(0);
            WorkflowRangeResult result = startingWorkflow.applyRange(segment);
            while (!result.isHandled()) {
                WorkflowRangeResult finalResult = result;
                Workflow nextWorkflow = workflows.stream().filter(w -> w.name().equals(finalResult.getNextWorkflowName())).findAny().orElseThrow();
                result = nextWorkflow.applyRange(segment);
            }
            if (result.isAccepted()) {
                runningTotal += segment.values().stream().mapToLong(p -> p.getRight() - p.getLeft()).reduce(1L, (a, b) -> a * b);
            }
            if (result.needsSplit()) {
                int oldUpperBound = segment.get(result.getSplitChar()).getRight();
                if (segment.get(result.getSplitChar()).getLeft() < result.getSplitPoint()) {
                    segment.put(result.getSplitChar(), Pair.of(segment.get(result.getSplitChar()).getLeft(), result.getSplitPoint()));
                    unexploredSegments.add(segment);
                }
                if (result.getSplitPoint() < oldUpperBound) {
                    Map<Character, Pair<Integer, Integer>> newSegment = new HashMap<>(segment);
                    newSegment.put(result.getSplitChar(), Pair.of(result.getSplitPoint(), oldUpperBound));
                    unexploredSegments.add(newSegment);
                }
            }
        }

        return runningTotal;
    }

    private boolean passesWorkflow(Map<Character, Integer> part, Workflow startingWorkflow, Set<Workflow> workflows) {
        WorkflowResult result = startingWorkflow.apply(part);
        while (!result.isHandled()) {
            WorkflowResult finalResult = result;
            Workflow nextWorkflow = workflows.stream().filter(w -> w.name().equals(finalResult.getNextWorkflowName())).findAny().orElseThrow();
            result = nextWorkflow.apply(part);
        }
        return result.isAccepted();
    }

    private int addRatings(Set<Map<Character, Integer>> parts, Workflow startingWorkflow, Set<Workflow> workflows) {
        return parts.stream()
                .filter(p -> passesWorkflow(p, startingWorkflow, workflows))
                .mapToInt(p -> p.values().stream().reduce(0, Integer::sum))
                .sum();
    }

    private record Workflow(String name, List<Rule> rules) {

        public WorkflowResult apply(Map<Character, Integer> part) {
                int i = 0;
                RuleResult result = rules.get(i).apply(part);
                while (!result.isHandled()) {
                    result = rules.get(i++).apply(part);
                }
                return new WorkflowResult(result.isAccepted(), result.getNextWorkflowName() == null, result.getNextWorkflowName());
            }

            public WorkflowRangeResult applyRange(Map<Character, Pair<Integer, Integer>> partRange) {
                int i = 0;
                RuleRangeResult result = rules.get(i).applyRange(partRange);
                while (!result.isHandled()) {
                    result = rules.get(i++).applyRange(partRange);
                }
                return new WorkflowRangeResult(result.isAccepted(), result.getNextWorkflowName() == null, result.getNextWorkflowName(), result.needsSplit(), result.getSplitPoint(), result.getSplitChar());
            }
        }


    private static class Rule {
        private String nextWorkflowName;
        private final boolean shouldAccept;
        private final boolean shouldReject;
        private boolean shouldCompare;
        private boolean shouldAcceptOnPass;
        private boolean shouldRejectOnPass;
        private char propertyToCheck;
        private int compareSgn, compareVal;

        public Rule(String ruleText) {
            shouldAccept = "A".equals(ruleText);
            shouldReject = "R".equals(ruleText);
            if (!shouldAccept && !shouldReject) {
                shouldCompare = ruleText.contains(":");
                if (shouldCompare) {
                    propertyToCheck = ruleText.charAt(0);
                    compareSgn = ruleText.charAt(1) == '>' ? 1 : -1;
                    compareVal = Integer.parseInt(ruleText.split(":")[0].split("[<>]")[1], 10);
                    String passDest = ruleText.split(":")[1];
                    shouldAcceptOnPass = "A".equals(passDest);
                    shouldRejectOnPass = "R".equals(passDest);
                    if (!shouldAcceptOnPass && !shouldRejectOnPass) {
                        nextWorkflowName = passDest;
                    }
                } else {
                    nextWorkflowName = ruleText;
                }
            }
        }

        public RuleResult apply(Map<Character, Integer> part) {
            if (shouldAccept || shouldReject) return new RuleResult(shouldAccept, true, null);
            if (!shouldCompare) return new RuleResult(false, true, nextWorkflowName);
            int partPropVal = part.get(propertyToCheck);
            return generateRuleResult(partPropVal);
        }

        private RuleResult generateRuleResult(int partPropVal) {
            int compareResult = Integer.compare(partPropVal, compareVal);
            if (compareResult * compareSgn > 0) {
                if (shouldAcceptOnPass || shouldRejectOnPass) return new RuleResult(shouldAcceptOnPass, true, null);
                return new RuleResult(false, true, nextWorkflowName);
            }
            return new RuleResult(false, false, null);
        }

        public RuleRangeResult applyRange(Map<Character, Pair<Integer, Integer>> partRange) {
            if (shouldAccept || shouldReject) return new RuleRangeResult(shouldAccept, true, null, false, -1, propertyToCheck);
            if (!shouldCompare) return new RuleRangeResult(false, true, nextWorkflowName, false, -1, propertyToCheck);
            Pair<Integer, Integer> propertyRange = partRange.get(propertyToCheck);
            Pair<Integer, Integer> compareResults = Pair.of(Integer.compare(propertyRange.getLeft(), compareVal), Integer.compare(propertyRange.getRight() - 1, compareVal));
            /*
            If the two results have the same sign, then this range can be treated as a single value
            Possible outcomes:
            1. left val negative, right val positive. This is an automatic fail
            2. left val negative, right val 0. This is a fail iff we are checking for val < cutoff (right val alone is not less than the cutoff point, but the cutoff point is not greater than any points in range)
            3. left val negative, right val negative. This is an automatic pass
            4. left val 0, right val 0. This is only possible if the range is exactly of size 1, so it's an automatic pass.
            5. left val 0, right val positive. This is a fail iff we are checking for val > cutoff (left val alone is not greater than the cutoff point, but the cutoff point is not less than all points in range)
            6. left val positive, right val positive. This is an automatic pass
            bit hack accounts for cases 1, 3, 4, and 6. need to confirm if exactly one of the values is zero
             */

            if ((compareResults.getLeft() == 0) ^ (compareResults.getRight() == 0)) {
                if (compareResults.getRight() == 0) {
                    if (compareSgn == 1) {
                        RuleResult ruleResult = generateRuleResult(propertyRange.getLeft());
                        return new RuleRangeResult(ruleResult.isAccepted(), ruleResult.isHandled(), ruleResult.getNextWorkflowName(), false, -1, propertyToCheck);
                    }
                    return needsSplit();
                }
                if (compareSgn == 1) {
                    return needsSplit();
                }
                RuleResult ruleResult = generateRuleResult(propertyRange.getLeft());
                return new RuleRangeResult(ruleResult.isAccepted(), ruleResult.isHandled(), ruleResult.getNextWorkflowName(), false, -1, propertyToCheck);
            }

            // https://graphics.stanford.edu/~seander/bithacks.html#DetectOppositeSigns
            if ((compareResults.getLeft() ^ compareResults.getRight()) >= 0) {
                RuleResult ruleResult = generateRuleResult(propertyRange.getLeft());
                return new RuleRangeResult(ruleResult.isAccepted(), ruleResult.isHandled(), ruleResult.getNextWorkflowName(), false, -1, propertyToCheck);
            }
            return needsSplit();
        }

        private RuleRangeResult needsSplit() {
            int splitPoint = compareSgn == 1 ? compareVal + 1 : compareVal;
            return new RuleRangeResult(false, true, null, true, splitPoint, propertyToCheck);
        }
    }

    private static class RuleResult extends BaseResult {
        public RuleResult(boolean accepted, boolean handled, String nextWorkflowName) {
            super(accepted, handled, nextWorkflowName);
        }
    }

    private static class WorkflowResult extends BaseResult {
        public WorkflowResult(boolean accepted, boolean handled, String nextWorkflowName) {
            super(accepted, handled, nextWorkflowName);
        }
    }

    private static class BaseResult {
        private final boolean accepted;
        private final boolean handled;
        private final String nextWorkflowName;

        public BaseResult(boolean accepted, boolean handled, String nextWorkflowName) {
            this.accepted = accepted;
            this.handled = handled;
            this.nextWorkflowName = nextWorkflowName;
        }

        public boolean isAccepted() {
            return accepted;
        }

        public boolean isHandled() {
            return handled;
        }

        public String getNextWorkflowName() {
            return nextWorkflowName;
        }
    }

    private static class WorkflowRangeResult extends BaseRangeResult {
        public WorkflowRangeResult(boolean accepted, boolean handled, String nextWorkflowName, boolean needsSplit, int splitPoint, char splitChar) {
            super(accepted, handled, nextWorkflowName, needsSplit, splitPoint, splitChar);
        }
    }

    private static class RuleRangeResult extends BaseRangeResult {
        public RuleRangeResult(boolean accepted, boolean handled, String nextWorkflowName, boolean needsSplit, int splitPoint, char splitChar) {
            super(accepted, handled, nextWorkflowName, needsSplit, splitPoint, splitChar);
        }
    }

    private static class BaseRangeResult extends BaseResult {
        private final boolean needsSplit;
        private final int splitPoint;
        private final char splitChar;

        public BaseRangeResult(boolean accepted, boolean handled, String nextWorkflowName, boolean needsSplit, int splitPoint, char splitChar) {
            super(accepted, handled, nextWorkflowName);
            this.needsSplit = needsSplit;
            this.splitPoint = splitPoint;
            this.splitChar = splitChar;
        }

        public boolean needsSplit() {
            return needsSplit;
        }

        public int getSplitPoint() {
            return splitPoint;
        }

        public char getSplitChar() {
            return splitChar;
        }
    }
}
