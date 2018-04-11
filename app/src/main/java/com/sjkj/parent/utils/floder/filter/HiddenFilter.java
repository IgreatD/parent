package com.sjkj.parent.utils.floder.filter;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;

public class HiddenFilter implements FileFilter, Serializable {

    @Override
    public boolean accept(File f) {
        return !f.isHidden();
    }
}
