package com.creativeartie.humming.document;

import java.util.*;

import com.creativeartie.humming.document.IdentitySpan.*;

public abstract class SpanBranchTestBase<T extends SpanBranch> extends SpanTestBase<T> {
    private static class HolderSpan extends SpanBranch implements IdentityParent {
        protected HolderSpan(Manuscript root, SpanStyle... classes) {
            super(root, classes);
        }

        @Override
        public int getIdPosition() {
            return 0;
        }

        @Override
        public Optional<IdentitySpan> getPointer() {
            // TODO Auto-generated method stub
            return null;
        }
    }

    protected T newSpan(String input) {
        T span = initSpan(new HolderSpan(getDocument()), input);
        printSpan(span, input);
        return span;
    }

    protected abstract T initSpan(SpanBranch parent, String input);
}
