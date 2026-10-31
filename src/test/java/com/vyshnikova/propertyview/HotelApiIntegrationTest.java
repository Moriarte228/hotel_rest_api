package com.vyshnikova.propertyview;

import com.jayway.jsonpath.JsonPath;
import com.vyshnikova.propertyview.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class HotelApiIntegrationTest {

    private static final String DOUBLETREE = """
            {
              "name": "DoubleTree by Hilton Minsk",
              "description": "Hotel in the centre of Minsk",
              "brand": "Hilton",
              "address": {"houseNumber": 9, "street": "Pobediteley Avenue", "city": "Minsk",
                          "country": "Belarus", "postCode": "220004"},
              "contacts": {"phone": "+375 17 309-80-00", "email": "doubletreeminsk.info@hilton.com"},
              "arrivalTime": {"checkIn": "14:00", "checkOut": "12:00"}
            }
            """;

    private static final String HILTON_BAKU = """
            {
              "name": "Hilton Baku",
              "brand": "Hilton",
              "address": {"houseNumber": 1, "street": "Azadlig Avenue", "city": "Baku",
                          "country": "Azerbaijan", "postCode": "AZ1000"},
              "contacts": {"phone": "+994 12 464-50-00", "email": "info@hilton-baku.example"},
              "arrivalTime": {"checkIn": "15:00"}
            }
            """;

    private static final String METROPOL = """
            {
              "name": "Hotel Metropol Moscow",
              "brand": "Independent",
              "address": {"houseNumber": 2, "street": "Teatralny Proezd", "city": "Moscow",
                          "country": "Russia", "postCode": "109012"},
              "contacts": {"phone": "+7 499 501-78-00", "email": "info@metropol-moscow.example"},
              "arrivalTime": {"checkIn": "14:00"}
            }
            """;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private HotelRepository repository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    private long createHotel(String json) throws Exception {
        String response = mockMvc.perform(post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return ((Number) JsonPath.read(response, "$.id")).longValue();
    }

    private void addAmenities(long hotelId, String json) throws Exception {
        mockMvc.perform(post("/hotels/{id}/amenities", hotelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void createHotel_returnsShortInfo() throws Exception {
        mockMvc.perform(post("/hotels").contentType(MediaType.APPLICATION_JSON).content(DOUBLETREE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("DoubleTree by Hilton Minsk"))
                .andExpect(jsonPath("$.address").value("9 Pobediteley Avenue, Minsk, 220004, Belarus"))
                .andExpect(jsonPath("$.phone").value("+375 17 309-80-00"));
    }

    @Test
    void createHotel_withoutRequiredFields_returns400() throws Exception {
        mockMvc.perform(post("/hotels").contentType(MediaType.APPLICATION_JSON).content("{\"brand\": \"Hilton\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").exists())
                .andExpect(jsonPath("$.errors.address").exists());
    }

    @Test
    void getAllHotels_returnsAllCreatedHotels() throws Exception {
        createHotel(DOUBLETREE);
        createHotel(HILTON_BAKU);

        mockMvc.perform(get("/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getHotelById_returnsFullInfo() throws Exception {
        long id = createHotel(DOUBLETREE);

        mockMvc.perform(get("/hotels/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("Hilton"))
                .andExpect(jsonPath("$.address.city").value("Minsk"))
                .andExpect(jsonPath("$.contacts.email").value("doubletreeminsk.info@hilton.com"))
                .andExpect(jsonPath("$.arrivalTime.checkIn").value("14:00"))
                .andExpect(jsonPath("$.amenities").isEmpty());
    }

    @Test
    void getHotelById_unknownId_returns404() throws Exception {
        mockMvc.perform(get("/hotels/{id}", 999999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Hotel with id 999999 not found"));
    }

    @Test
    void addAmenities_ignoresDuplicatesAndKeepsOrder() throws Exception {
        long id = createHotel(DOUBLETREE);

        addAmenities(id, "[\"Free parking\", \"Free WiFi\", \"Free WiFi\"]");
        addAmenities(id, "[\"Spa\", \"Free parking\"]");

        mockMvc.perform(get("/hotels/{id}", id))
                .andExpect(jsonPath("$.amenities", contains("Free parking", "Free WiFi", "Spa")));
    }

    @Test
    void search_byCity_isCaseInsensitive() throws Exception {
        createHotel(DOUBLETREE);
        createHotel(HILTON_BAKU);

        mockMvc.perform(get("/search").param("city", "minsk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("DoubleTree by Hilton Minsk"));
    }

    @Test
    void search_byNamePart() throws Exception {
        createHotel(DOUBLETREE);
        createHotel(HILTON_BAKU);
        createHotel(METROPOL);

        mockMvc.perform(get("/search").param("name", "hilton"))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void search_byAmenities_requiresAllOfThem() throws Exception {
        long minsk = createHotel(DOUBLETREE);
        long baku = createHotel(HILTON_BAKU);
        addAmenities(minsk, "[\"Free WiFi\"]");
        addAmenities(baku, "[\"Free WiFi\", \"Spa\"]");

        mockMvc.perform(get("/search").param("amenities", "Free WiFi", "Spa"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Hilton Baku"));
    }

    @Test
    void histogram_countsHotelsByBrand() throws Exception {
        createHotel(DOUBLETREE);
        createHotel(HILTON_BAKU);
        createHotel(METROPOL);

        mockMvc.perform(get("/histogram/brand"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Hilton").value(2))
                .andExpect(jsonPath("$.Independent").value(1));
    }

    @Test
    void histogram_countsHotelsByAmenity() throws Exception {
        long minsk = createHotel(DOUBLETREE);
        long baku = createHotel(HILTON_BAKU);
        addAmenities(minsk, "[\"Free WiFi\", \"Spa\"]");
        addAmenities(baku, "[\"Free WiFi\"]");

        mockMvc.perform(get("/histogram/amenities"))
                .andExpect(jsonPath("$['Free WiFi']").value(2))
                .andExpect(jsonPath("$.Spa").value(1));
    }

    @Test
    void histogram_unsupportedParam_returns400() throws Exception {
        mockMvc.perform(get("/histogram/stars"))
                .andExpect(status().isBadRequest());
    }
}