package com.creativeartie.humming.document;

import java.util.*;
import java.util.Optional;

import com.google.common.base.*;

public class IdentityStorage {
    public interface Identity {
        public abstract IdentityGroup getIdGroup();

        public abstract List<String> getCategories();

        public abstract String getId();

        public default String getFullId() {
            List<String> ids = getCategories();
            if (ids.isEmpty()) return getId();

            return Joiner.on(":").join(ids) + ":" + getId();
        }

        public default String getInternalId() {
            return getIdGroup().name() + ":" + getFullId();
        }

        public abstract boolean isPointer();

        public default boolean isAddress() {
            return !isPointer();
        }

        public abstract int getPosition();
    }

    public class Manager implements Comparable<Manager> {
        private ArrayList<Identity> pointerIds;
        private ArrayList<Identity> addressIds;
        private final String fullIdName;

        private Manager(String name) {
            pointerIds = new ArrayList<>();
            addressIds = new ArrayList<>();
            fullIdName = name;
        }

        public boolean isCorrect() {
            return addressIds.size() == 1;
        }

        public String getFullId() {
            return fullIdName;
        }

        public void cleanUpIds() {
            for (Identity ptr : pointerIds) {
                ((Span) ptr).cleanUp();
            }
            for (Identity address : addressIds) {
                ((Span) address).cleanUp();
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Manager) {
                return ((Manager) obj).fullIdName == fullIdName;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return fullIdName.hashCode();
        }

        @Override
        public int compareTo(Manager o) {
            return fullIdName.compareTo(o.fullIdName);
        }
    }

    private TreeSet<Manager> idManager;
    private Optional<IdentityStorage> parentStorage;

    public IdentityStorage(IdentityStorage parent) {
        idManager = new TreeSet<>();
        parentStorage = Optional.of(parent);
    }

    public IdentityStorage() {
        idManager = new TreeSet<>();
        parentStorage = Optional.empty();
    }

    public boolean isIdUnique(Identity span) {
        Manager name = new Manager(span.getInternalId());
        Manager manager = idManager.floor(name);
        boolean isUnique = false;
        if (manager != null) {
            isUnique = manager.addressIds.size() == 1;
        }
        if (parentStorage.isPresent()) {
            boolean isParentUnique = parentStorage.get().isIdUnique(span);
            if (isUnique) {
                return !isParentUnique;
            } else {
                return isParentUnique;
            }
        }
        return isUnique;
    }

    public int getPointerCount(Identity span) {
        Manager name = new Manager(span.getInternalId());
        Manager manager = idManager.floor(name);
        int count = 0;
        if (manager != null) {
            manager.pointerIds.size();
        }
        if (parentStorage.isPresent()) {
            count += parentStorage.get().getPointerCount(span);
        }
        return count;
    }

    public void addId(Identity id) {
        Manager name = new Manager(id.getInternalId());
        Manager manager = idManager.floor(name);
        if (manager == null) {
            manager = name;
            idManager.add(manager);
        }
        if (id.isPointer()) {
            manager.pointerIds.add(id);
        } else {
            manager.addressIds.add(id);
        }
    }

    public void removeId(Identity id) {
        Manager name = new Manager(id.getInternalId());
        Manager manager = idManager.floor(name);
        if (manager == null) return;
        if (id.isPointer()) {
            manager.pointerIds.remove(id);
        } else {
            manager.addressIds.remove(id);
        }
        if (manager.pointerIds.isEmpty() && manager.addressIds.isEmpty()) {
            idManager.remove(manager);
        }
    }

    public void clear() {
        idManager.clear();
    }
}
