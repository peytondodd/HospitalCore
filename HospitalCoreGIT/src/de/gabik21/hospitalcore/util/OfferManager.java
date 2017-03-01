package de.gabik21.hospitalcore.util;

import java.util.List;

import de.gabik21.hospitalcore.types.Offer;

import java.util.ArrayList;

public class OfferManager {

    private static OfferManager instance;
    private List<Offer> offers = new ArrayList<Offer>();

    protected OfferManager() {
    }

    public static void init() {
	instance = new OfferManager();
    }

    public static OfferManager inst() {
	return instance;
    }

    public List<Offer> getOffers() {
	return offers;
    }

    public List<Offer> searchForOffers(String kit) {

	List<Offer> temp = new ArrayList<Offer>();

	for (Offer offer : getOffers()) {
	    if (offer.getKit().getName().equals(kit))
		temp.add(offer);

	}
	return temp;
    }

}
