package de.gabik21.hospitalcore.types;

import java.io.Serializable;
import java.util.UUID;

public class Offer implements Serializable, Comparable<Offer> {

    private static final long serialVersionUID = 550159301481306803L;
    private String nameOfferedBy;
    private UUID offeredBy, id;
    private Kit kit;
    private long price;

    public Offer(UUID offeredBy, Kit kit, long price, String nameOfferedBy) {

	this.kit = kit;
	this.offeredBy = offeredBy;
	this.nameOfferedBy = nameOfferedBy;
	this.price = price;
	this.id = UUID.randomUUID();

    }

    public long getPrice() {
	return price;
    }

    public UUID getOfferedBy() {
	return offeredBy;
    }

    public String getNameOfferedBy() {
	return nameOfferedBy;
    }

    public Kit getKit() {
	return kit;
    }

    public UUID getUniqueId() {
	return id;
    }

    public int compareTo(Offer offer) {
	return this.price <= offer.getPrice() ? 0 : 1;
    }

    @Override
    public boolean equals(Object obj) {
	return obj instanceof Offer ? id.equals(((Offer) obj).getUniqueId()) : false;
    }

}
