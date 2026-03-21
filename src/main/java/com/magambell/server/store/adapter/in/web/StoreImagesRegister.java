package com.magambell.server.store.adapter.in.web;

public record StoreImagesRegister(
        Integer id,
                String key,
                String imageUrl
) {
        public StoreImagesRegister(final Integer id, final String key) {
                this(id, key, null);
        }
}
