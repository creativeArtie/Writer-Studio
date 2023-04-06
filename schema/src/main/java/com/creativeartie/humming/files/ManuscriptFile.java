package com.creativeartie.humming.files;

import java.util.*;

import com.creativeartie.humming.document.*;

/**
 * Information about the writing.
 *
 * @author wai
 */
public class ManuscriptFile {
    private String draftName;
    private Manuscript mainWriting;
    private int draftNumber;
    private Optional<ManuscriptFile> previousDraft;
    private Optional<ManuscriptFile> nextDraft;

    ManuscriptFile(String name) {
        draftName = name;
        draftNumber = 1;
        mainWriting = new Manuscript();
        previousDraft = Optional.empty();
        nextDraft = Optional.empty();
    }

    ManuscriptFile(ManuscriptFile previous) {
        draftNumber = previous.draftNumber + 1;
        draftName = previous.draftName;
        mainWriting = new Manuscript();
        previousDraft = Optional.ofNullable(previous);
        nextDraft = Optional.empty();
        previous.nextDraft = Optional.of(this);
    }

    /**
     * Get the next draft.
     *
     * @return the next draft
     */
    public Optional<ManuscriptFile> getNextDraft() {
        return nextDraft;
    }

    /**
     * Get the previous draft
     *
     * @return the previous draft
     */
    public Optional<ManuscriptFile> getPreviousDraft() {
        return previousDraft;
    }

    /**
     * Get the draft name
     *
     * @return draft name
     */
    public String getDraftName() {
        return draftName;
    }

    /**
     * Get the draft number
     *
     * @return draft number
     */
    public int getDraftNumber() {
        return draftNumber;
    }

    /**
     * Is the draft is archived.
     *
     * @return {@code true} if archived.
     */
    public boolean isArchived() {
        return nextDraft.isEmpty();
    }

    /**
     * Gets the manuscript
     *
     * @return manuscript
     */
    public Manuscript getManuscript() {
        return mainWriting;
    }
}
