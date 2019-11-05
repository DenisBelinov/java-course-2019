package bg.sofia.uni.fmi.mjt.jira.issues;

import bg.sofia.uni.fmi.mjt.jira.enums.IssuePriority;
import bg.sofia.uni.fmi.mjt.jira.enums.IssueResolution;
import bg.sofia.uni.fmi.mjt.jira.enums.IssueStatus;
import bg.sofia.uni.fmi.mjt.jira.enums.WorkAction;

public class Bug extends Issue {
    public Bug(IssuePriority issuePriority, Component component, String description) {
        super(issuePriority, component, description);
    }

    @Override
    public void resolve(IssueResolution resolution) {
        if (!hasAction(WorkAction.FIX) || !hasAction(WorkAction.TESTS)) {
            String errMsg = String.format("Cannot resolve Bug %s .", this.getIssueID());
            throw new RuntimeException(errMsg);
        }

        this.issueResolution = resolution;
        setStatus(IssueStatus.RESOLVED);
        updateModificationDate();
    }
}
