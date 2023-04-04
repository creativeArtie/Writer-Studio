package com.creativeartie.humming.document;

import java.util.*;
import java.util.Optional;

import com.google.common.base.*;

/**
 * Stores a list of identities. It can contain a parent
 * {@linkplain IdentityStorage}.
 */
public final class IdentityStorage {
    /**
     * An single Identity
     */
    public interface Identity {
        /**
         * Get the user defined categories
         *
         * @return categories
         */
        List<String> getCategories();

        /**
         * Get the full user defined id
         *
         * @return full id with {@link #getCategories()} and {@link #getId()}.
         */
        default String getFullId() {
            List<String> ids = getCategories();
            if (ids.isEmpty()) return getId();

            return Joiner.on(":").join(ids) + ":" + getId();
        }

        /**
         * Get the user defined id
         *
         * @return id name
         */
        String getId();

        /**
         * The id group that this id is part of
         *
         * @return identity group like footnote and agenda
         */
        IdentityGroup getIdGroup();

        /**
         * Get the id for internal use
         *
         * @return id with {@link #getIdGroup()} and {@link #getFullId()}.
         */
        default String getInternalId() {
            return getIdGroup().name() + ":" + getFullId();
        }

        /**
         * Get the position of the id span.
         *
         * @return the id span
         */
        int getPosition();

        /**
         * Is the is an ID address.
         *
         * @return {@code true} if this is a address
         */
        default boolean isAddress() {
            return !isPointer();
        }

        /**
         * Is the is an ID pointer.
         *
         * @return {@code true} if this is a pointer
         */
        boolean isPointer();
    }

    /**
     * Manages a single id with its pointers and addresses.
     */
    private static class Manager implements Comparable<Manager> {
        private final ArrayList<Identity> pointerIds;
        private final ArrayList<Identity> addressIds;
        private final String fullIdName;

        private Manager(String name) {
            pointerIds = new ArrayList<>();
            addressIds = new ArrayList<>();
            fullIdName = name;
        }

        @Override
        public int compareTo(Manager o) {
            return fullIdName.compareTo(o.fullIdName);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Manager) return ((Manager) obj).fullIdName == fullIdName;
            return false;
        }

        @Override
        public int hashCode() {
            return fullIdName.hashCode();
        }
    }

    private final TreeSet<Manager> idManager;
    private final Optional<IdentityStorage> parentStorage;

    /**
     * Constructor without a parent
     */
    public IdentityStorage() {
        idManager = new TreeSet<>();
        parentStorage = Optional.empty();
    }

    /**
     * Constructor with a parent
     *
     * @param parent
     *        the parent storage
     */
    public IdentityStorage(IdentityStorage parent) {
        idManager = new TreeSet<>();
        parentStorage = Optional.ofNullable(parent);
    }

    /**
     * Adds an ID
     *
     * @param id
     *        the id to add
     */
    public void addId(Identity id) {
        Manager name = new Manager(id.getInternalId());
        Manager manager = idManager.floor(name);
        if (manager == null) {
            manager = name;
            idManager.add(manager);
        }
        if (id.isPointer()) manager.pointerIds.add(id);
        else manager.addressIds.add(id);
    }

    /**
     * Clear the list of ID.
     */
    public void clear() {
        idManager.clear();
    }

    /**
     * Get the ID pointer count
     *
     * @param span
     *        the id to check
     *
     * @return id count
     */
    public int getPointerCount(Identity span) {
        Manager name = new Manager(span.getInternalId());
        Manager manager = idManager.floor(name);
        int count = 0;
        if (manager != null) manager.pointerIds.size();
        if (parentStorage.isPresent()) count += parentStorage.get().getPointerCount(span);
        return count;
    }

    /**
     * Check an ID is unique. That is the ID only have single address
     *
     * @param span
     *        the id to check
     *
     * @return {@code true} if it is unique
     */
    public boolean isIdUnique(Identity span) {
        Manager name = new Manager(span.getInternalId());
        Manager manager = idManager.floor(name);
        boolean isUnique = false;
        if (manager != null) isUnique = manager.addressIds.size() == 1;
        if (parentStorage.isPresent()) {
            boolean isParentUnique = parentStorage.get().isIdUnique(span);
            if (isUnique) return !isParentUnique;
            return isParentUnique;
        }
        return isUnique;
    }

    /**
     * Removes an ID
     *
     * @param id
     *        the id to remove
     */
    public void removeId(Identity id) {
        Manager name = new Manager(id.getInternalId());
        Manager manager = idManager.floor(name);
        if (manager == null) return;
        if (id.isPointer()) manager.pointerIds.remove(id);
        else manager.addressIds.remove(id);
        if (manager.pointerIds.isEmpty() && manager.addressIds.isEmpty()) idManager.remove(manager);
    }
}
