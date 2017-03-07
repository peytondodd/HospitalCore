package de.gabik21.hospitalcore.types;

import java.io.Serializable;
import java.util.UUID;

public class Offer implements Serializable, Comparable<Offer> {

    private static final long serialVersionUID = 550159301481306803L;
    private String nameOfferedBy, adress;
    private UUID offeredBy, id;
    private Kit kit;
    private long price;

    public Offer(UUID offeredBy, Kit kit, long price, String nameOfferedBy, String adress) {

	this.kit = kit;
	this.offeredBy = offeredBy;
	this.nameOfferedBy = nameOfferedBy;
	this.price = price;
	this.id = UUID.randomUUID();
	this.adress = adress;

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

    public String getAdress() {
	return adress;
    }

    public void setAdress(String adress) {
	this.adress = adress;
    }

    public Kit getKit() {
	return kit;
    }

    public UUID getUniqueId() {
	return id;
    }

    public int compareTo(Offer offer) {
	return (int) (getPrice() - offer.getPrice());
    }

    @Override
    public boolean equals(Object obj) {
	return obj instanceof Offer ? id.equals(((Offer) obj).getUniqueId()) : false;
    }

}
