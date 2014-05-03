package com.github.derwisch.paperMail;

public enum SendingGUIClickResult {
    /**
     * Do nothing
     */
    NOTHING,
   
    /**
     * Send mail
     */
    SEND,
   
    /**
     * Cancel mail
     */
    CANCEL,
   
    /**
     * Open Enderchest, continue mail afterwards
     */
    OPEN_ENDERCHEST
}
