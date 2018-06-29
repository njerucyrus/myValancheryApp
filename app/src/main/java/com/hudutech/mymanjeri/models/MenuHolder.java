package com.hudutech.mymanjeri.models;

import com.hudutech.mymanjeri.LoginActivity;
import com.hudutech.mymanjeri.classifields_activities.OthersActivity;
import com.hudutech.mymanjeri.classifields_activities.PetsActivity;
import com.hudutech.mymanjeri.classifields_activities.RealEstateActivity;
import com.hudutech.mymanjeri.conctact_activities.ArtAndCultureActivity;
import com.hudutech.mymanjeri.conctact_activities.BanksActivity;
import com.hudutech.mymanjeri.conctact_activities.EmergencyActivity;
import com.hudutech.mymanjeri.conctact_activities.GeneralActivity;
import com.hudutech.mymanjeri.conctact_activities.InstitutionsActivity;
import com.hudutech.mymanjeri.conctact_activities.LabouresActivity;
import com.hudutech.mymanjeri.conctact_activities.MediaActivity;
import com.hudutech.mymanjeri.conctact_activities.MlaActivity;
import com.hudutech.mymanjeri.conctact_activities.MpActivity;
import com.hudutech.mymanjeri.conctact_activities.MunicipalityActivity;
import com.hudutech.mymanjeri.conctact_activities.PoliticsActivity;
import com.hudutech.mymanjeri.conctact_activities.ProfessionalsActivity;
import com.hudutech.mymanjeri.conctact_activities.VehicleActivity;
import com.hudutech.mymanjeri.digital_activities.SBAdminPanelActivity;
import com.hudutech.mymanjeri.majery_activities.BloodBankActivity;
import com.hudutech.mymanjeri.majery_activities.EducationActivity;
import com.hudutech.mymanjeri.majery_activities.HistoryActivity;
import com.hudutech.mymanjeri.majery_activities.NewsActivity;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.majery_activities.NotificationActivity;
import com.hudutech.mymanjeri.majery_activities.ShoppingActivity;
import com.hudutech.mymanjeri.majery_activities.TourismActivity;
import com.hudutech.mymanjeri.medical_activities.DoctorsActivity;
import com.hudutech.mymanjeri.medical_activities.HospitalsActivity;
import com.hudutech.mymanjeri.medical_activities.LabsActivity;
import com.hudutech.mymanjeri.medical_activities.MedicalShopsActivity;
import com.hudutech.mymanjeri.medical_activities.OpticalActivity;
import com.hudutech.mymanjeri.timing_and_booking_activities.BusActivity;
import com.hudutech.mymanjeri.timing_and_booking_activities.FilmActivity;
import com.hudutech.mymanjeri.timing_and_booking_activities.HotelsActivity;
import com.hudutech.mymanjeri.timing_and_booking_activities.RestaurantsActivity;
import com.hudutech.mymanjeri.timing_and_booking_activities.TrainActivity;
import com.hudutech.mymanjeri.timing_and_booking_activities.TravelsActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuHolder {
    private HashMap<String, List<CategoryMenu>> menuHashMap = new HashMap<>();
    private static final String CATEGORY_MAJERY = "manjery";
    private static final String CATEGORY_CONTACTS_1 = "contacts_1";
    private static final String CATEGORY_CONTACTS_2 = "contacts_2";
    private static final String CATEGORY_TIMING_AND_BOOKING = "timing_and_booking";
    private static final String CATEGORY_CLASSIFIEDS = "classifieds";
    private static final String CATEGORY_MEDICAL = "medical";
    private static final String CATEGORY_DIGITAL = "digital";


    public MenuHolder() {

        //create the menu object when the object instance is created
        initMajeryMenus();
        initContacts1Menus();
        initContact2Menus();
        initTimingAndBooking();
        initClassifields();
        initMedical();
        initDigital();

    }

    public HashMap<String, List<CategoryMenu>> getMenuHashMap() {
        return menuHashMap;
    }

    private void initMajeryMenus() {
        ArrayList<CategoryMenu> majeryMenus = new ArrayList<>();

        CategoryMenu notificationMenu = new CategoryMenu(
                "Majery", "Notification", NotificationActivity.class, R.drawable.no_icon_48
        );
        majeryMenus.add(notificationMenu);

        CategoryMenu newsMenu = new CategoryMenu(
                "Majery", "News", NewsActivity.class, R.drawable.news_48
        );
        majeryMenus.add(newsMenu);

        CategoryMenu bloodBankMenu = new CategoryMenu(
                "Majery", "Blood Bank", BloodBankActivity.class, R.drawable.blood_bank_48
        );
        majeryMenus.add(bloodBankMenu);

        CategoryMenu tourismMenu = new CategoryMenu(
                "Majery", "Tourism", TourismActivity.class, R.drawable.tourism_48
        );

        majeryMenus.add(tourismMenu);

        CategoryMenu historyMenu = new CategoryMenu(
                "Majery", "History", HistoryActivity.class, R.drawable.history_48
        );

        majeryMenus.add(historyMenu);


        CategoryMenu shopping = new CategoryMenu(
                "Majery", "Shopping", ShoppingActivity.class, R.drawable.shopping_48
        );

        majeryMenus.add(shopping);


        CategoryMenu education = new CategoryMenu(
                "Majery", "Education", EducationActivity.class, R.drawable.education_48
        );

        majeryMenus.add(education);

        this.menuHashMap.put(CATEGORY_MAJERY, majeryMenus);

    }

    private void initContacts1Menus() {
        ArrayList<CategoryMenu> contact1Menus = new ArrayList<>();
        CategoryMenu vehicle = new CategoryMenu(
                "Contact1", "Vehicle", VehicleActivity.class, R.drawable.vehicles_48
        );
        contact1Menus.add(vehicle);

        CategoryMenu emmergency = new CategoryMenu(
                "Contact1", "Emergency", EmergencyActivity.class, R.drawable.emergency_48
        );
        contact1Menus.add(emmergency);

        CategoryMenu general = new CategoryMenu(
                "Contact1", "General", GeneralActivity.class, R.drawable.no_icon_48
        );
        contact1Menus.add(general);


        CategoryMenu mp = new CategoryMenu(
                "Contact1", "MP", MpActivity.class, R.drawable.mp_48
        );
        contact1Menus.add(mp);

        CategoryMenu mla = new CategoryMenu(
                "Contact1", "MLA", MlaActivity.class, R.drawable.mla_48
        );
        contact1Menus.add(mla);


        CategoryMenu politics = new CategoryMenu(
                "Contact1", "Politics", PoliticsActivity.class, R.drawable.no_icon_48
        );
        contact1Menus.add(politics);


        CategoryMenu media = new CategoryMenu(
                "Contact1", "Media", MediaActivity.class, R.drawable.no_icon_48
        );
        contact1Menus.add(media);


        CategoryMenu art_and_culture = new CategoryMenu(
                "Contact1", "Art & Culture", ArtAndCultureActivity.class, R.drawable.no_icon_48
        );
        contact1Menus.add(art_and_culture);

        this.menuHashMap.put(CATEGORY_CONTACTS_1, contact1Menus);

    }

    private void initContact2Menus() {

        List<CategoryMenu> contact2 = new ArrayList<>();
        CategoryMenu municipality = new CategoryMenu(
                "Contact2", "Municipality", MunicipalityActivity.class, R.drawable.no_icon_48
        );
        contact2.add(municipality);

        CategoryMenu banks = new CategoryMenu(
                "Contact2", "Banks", BanksActivity.class, R.drawable.bank_48
        );

        contact2.add(banks);

        CategoryMenu professionals = new CategoryMenu(
                "Contact2", "Professionals", ProfessionalsActivity.class, R.drawable.no_icon_48
        );

        contact2.add(professionals);

        CategoryMenu labourers = new CategoryMenu(
                "Contact2", "Labourers", LabouresActivity.class, R.drawable.labourers_48
        );

        contact2.add(labourers);


        CategoryMenu institutions = new CategoryMenu(
                "Contact2", "Institutions", InstitutionsActivity.class, R.drawable.institution_48
        );

        contact2.add(institutions);
        this.menuHashMap.put(CATEGORY_CONTACTS_2, contact2);

    }

    private void initTimingAndBooking() {
        List<CategoryMenu> timingAndBooking = new ArrayList<>();
        CategoryMenu bus = new CategoryMenu(
                "TimingAndBooking", "Bus", BusActivity.class, R.drawable.bus_48
        );
        timingAndBooking.add(bus);

        CategoryMenu train = new CategoryMenu(
                "TimingAndBooking", "Train", TrainActivity.class, R.drawable.train_48
        );

        timingAndBooking.add(train);

        CategoryMenu film = new CategoryMenu(
                "TimingAndBooking", "Film", FilmActivity.class, R.drawable.film_48
        );

        timingAndBooking.add(film);

        CategoryMenu hotel = new CategoryMenu(
                "TimingAndBooking", "Hotels", HotelsActivity.class, R.drawable.hotel_48
        );

        timingAndBooking.add(hotel);


        CategoryMenu travels = new CategoryMenu(
                "TimingAndBooking", "Travel", TravelsActivity.class, R.drawable.travel_48
        );
        timingAndBooking.add(travels);

        CategoryMenu restaurants = new CategoryMenu(
                "TimingAndBooking", "Restaurants", RestaurantsActivity.class, R.drawable.restulants_48
        );
        timingAndBooking.add(restaurants);


        this.menuHashMap.put(CATEGORY_TIMING_AND_BOOKING, timingAndBooking);
    }

    private void initClassifields() {
        List<CategoryMenu> classfields = new ArrayList<>();
        CategoryMenu realEstate = new CategoryMenu(
                "Classifieds", "Real Estate", RealEstateActivity.class, R.drawable.real_estate_48
        );
        classfields.add(realEstate);

        CategoryMenu vehicle = new CategoryMenu(
                "Classifieds", "Vehicle", VehicleActivity.class, R.drawable.vehicles_48
        );

        classfields.add(vehicle);

        CategoryMenu electronics = new CategoryMenu(
                "Classifieds", "Electronics", FilmActivity.class, R.drawable.no_icon_48
        );

        classfields.add(electronics);

        CategoryMenu pets = new CategoryMenu(
                "Classifieds", "Pets", PetsActivity.class, R.drawable.pets_48
        );

        classfields.add(pets);


        CategoryMenu others = new CategoryMenu(
                "Classifieds", "Others", OthersActivity.class, R.drawable.no_icon_48
        );
        classfields.add(others);


        this.menuHashMap.put(CATEGORY_CLASSIFIEDS, classfields);
    }

    private void initMedical() {

        List<CategoryMenu> medical = new ArrayList<>();
        CategoryMenu hospitals = new CategoryMenu(
                "Medical", "Hospitals", HospitalsActivity.class, R.drawable.hospital_48
        );
        medical.add(hospitals);

        CategoryMenu doctor= new CategoryMenu(
                "Medical", "Doctors", DoctorsActivity.class, R.drawable.doctor_48
        );

        medical.add(doctor);

        CategoryMenu labs = new CategoryMenu(
                "Medical", "Labs", LabsActivity.class, R.drawable.lab_48
        );

        medical.add(labs);

        CategoryMenu medicalShops = new CategoryMenu(
                "Medical", "Medical Shops", MedicalShopsActivity.class, R.drawable.medical_shops_48
        );

        medical.add(medicalShops);


        CategoryMenu opticals = new CategoryMenu(
                "Medical", "Optical", OpticalActivity.class, R.drawable.opticals_48
        );
        medical.add(opticals);


        this.menuHashMap.put(CATEGORY_MEDICAL, medical);
    }

    private void initDigital() {
        List<CategoryMenu> digitalMenu = new ArrayList<>();

        CategoryMenu sbBank = new CategoryMenu(
                "Digital", "S Bank", LoginActivity.class, R.drawable.no_icon_48
        );
        digitalMenu.add(sbBank);

        this.menuHashMap.put(CATEGORY_DIGITAL, digitalMenu);
    }
}
