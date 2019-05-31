/*=============================================================================
                    AUTOMATED LOGIC CORPORATION
            Copyright (c) 1999 - 2008 All Rights Reserved
     This document contains confidential/proprietary information.
===============================================================================

   @(#)CopyAction

   Author(s) sappling
=============================================================================*/
package org.gradle.api.tasks.copy;

import groovy.lang.Closure;

import java.io.File;
import java.io.FilterReader;
import java.util.List;
import java.util.Map;

public interface CopyAction extends CopySpec{

    /**
     * Execute the copy
     */
    void execute();

    /**
     * Checks if the copy actually copied any files.
     * @return true if any files were copied.
     */
    boolean getDidWork();

    /**
     * Set case sensitivity for comparisons.
     * @param caseSensitive
     */
    void setCaseSensitive(boolean caseSensitive);

    List<? extends CopySpec> getLeafSyncSpecs();

    CopySpec getRootSyncSpec();

}
