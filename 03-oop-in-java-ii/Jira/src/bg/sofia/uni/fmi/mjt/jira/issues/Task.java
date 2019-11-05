package bg.sofia.uni.fmi.mjt.jira.issues;

import bg.sofia.uni.fmi.mjt.jira.enums.IssuePriority;
import bg.sofia.uni.fmi.mjt.jira.enums.IssueResolution;
import bg.sofia.uni.fmi.mjt.jira.enums.IssueStatus;
import bg.sofia.uni.fmi.mjt.jira.enums.WorkAction;

public class Task extends Issue {

    public Task(IssuePriority issuePriority, Component component, String description) {
        super(issuePriority, component, description);
    }

    @Override
    public void resolve(IssueResolution resolution) {
        this.issueResolution = resolution;
        updateModificationDate();
        setStatus(IssueStatus.RESOLVED);
    }

    @Override
    public void addAction(WorkAction action, String description) {
        if (action != WorkAction.DESIGN && action != WorkAction.RESEARCH) {
            String errMsg = String.format("Failed to add action %s to %s. Tasks accepts only %s and %s actions",
                                        action.toString(),
                                        this.getIssueID(),
                                        WorkAction.DESIGN.toString(),
                                        WorkAction.RESEARCH.toString());
            throw new RuntimeException(errMsg);
        }
        super.addAction(action, description);
        updateModificationDate();
    }
}
