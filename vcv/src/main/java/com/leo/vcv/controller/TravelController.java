package com.leo.vcv.controller;

import com.leo.vcv.model.InternationalTravel;
import com.leo.vcv.model.NationalTravel;
import com.leo.vcv.model.Travel;
import com.leo.vcv.service.AccommodationService;
import com.leo.vcv.service.ClientService;
import com.leo.vcv.service.TransportService;
import com.leo.vcv.service.TravelService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
@RequestMapping("/travels")
@RequiredArgsConstructor
public class TravelController {
    private final TravelService travelService;
    private final ClientService clientService;
    private final TransportService transportService;
    private final AccommodationService accommodationService;


    private String showCreateForm(Model model, String type) {
        prepareTravelFormModel(model, type);
        return "travels/create-" + type;
    }


    private String processCreateTravel(
            Travel.TravelType type,
            String destination,
            LocalDate departureDate,
            LocalDate returnDate,
            BigDecimal price,
            Long clientId,
            Long transportId,
            Long accommodationId,
            Boolean needsVisa,
            RedirectAttributes redirectAttributes,
            String successMessage,
            String errorPrefix) {

        try {
            travelService.createTravel(
                    type,
                    destination,
                    departureDate,
                    returnDate,
                    price,
                    clientId,
                    transportId,
                    accommodationId,
                    needsVisa
            );
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", errorPrefix + e.getMessage());
        }
        return "redirect:/travels";
    }

    private String showEditForm(Long id, Model model, String type, Class<? extends Travel> travelClass) {
        try {
            Travel travel = travelService.getTravelById(id);
            if (!travelClass.isInstance(travel)) {
                return "redirect:/travels";
            }
            model.addAttribute("travel", travel);
            prepareTravelFormModel(model, type);
            return "travels/edit-" + type;
        } catch (EntityNotFoundException e) {
            return "redirect:/travels";
        }
    }

    @GetMapping
    public String listTravels(Model model) {
        model.addAttribute("travels", travelService.getAllTravels());
        return "travels/list";
    }

    @GetMapping("/national/create")
    public String showCreateNationalForm(Model model) {
        return showCreateForm(model, "national");
    }

    @GetMapping("/international/create")
    public String showCreateInternationalForm(Model model) {
        return showCreateForm(model, "international");
    }

    @PostMapping("/national")
    public String createNationalTravel(
            @RequestParam String destination,
            @RequestParam LocalDate departureDate,
            @RequestParam LocalDate returnDate,
            @RequestParam BigDecimal price,
            @RequestParam Long clientId,
            @RequestParam(required = false) Long transportId,
            @RequestParam(required = false) Long accommodationId,
            RedirectAttributes redirectAttributes) {

        return processCreateTravel(
                Travel.TravelType.NATIONAL,
                destination,
                departureDate,
                returnDate,
                price,
                clientId,
                transportId,
                accommodationId,
                null,
                redirectAttributes,
                "Viagem nacional criada com sucesso!",
                "Erro ao criar viagem nacional: "
        );
    }

    @PostMapping("/international")
    public String createInternationalTravel(
            @RequestParam String destination,
            @RequestParam LocalDate departureDate,
            @RequestParam LocalDate returnDate,
            @RequestParam BigDecimal price,
            @RequestParam Long clientId,
            @RequestParam(required = false) Long transportId,
            @RequestParam(required = false) Long accommodationId,
            @RequestParam boolean needsVisa,
            RedirectAttributes redirectAttributes) {

        return processCreateTravel(
                Travel.TravelType.INTERNATIONAL,
                destination,
                departureDate,
                returnDate,
                price,
                clientId,
                transportId,
                accommodationId,
                needsVisa,
                redirectAttributes,
                "Viagem internacional criada com sucesso!",
                "Erro ao criar viagem internacional: "
        );
    }

    @GetMapping("/{id}")
    public String viewTravel(@PathVariable Long id, Model model) {
        try {
            Travel travel = travelService.getTravelById(id);
            model.addAttribute("travel", travel);
            return "travels/view";
        } catch (EntityNotFoundException e) {
            return "redirect:/travels";
        }
    }

    @GetMapping("/national/edit/{id}")
    public String showEditNationalForm(@PathVariable Long id, Model model) {
        return showEditForm(id, model, "national", NationalTravel.class);
    }

    @GetMapping("/international/edit/{id}")
    public String showEditInternationalForm(@PathVariable Long id, Model model) {
        return showEditForm(id, model, "international", InternationalTravel.class);
    }

    @PostMapping("/national/update/{id}")
    public String updateNationalTravel(
            @PathVariable Long id,
            @ModelAttribute NationalTravel travel,
            RedirectAttributes redirectAttributes) {

        try {
            travelService.updateTravel(
                    id,
                    Travel.TravelType.NATIONAL,
                    travel.getDestination(),
                    travel.getDepartureDate(),
                    travel.getReturnDate(),
                    travel.getPrice(),
                    travel.getClient().getId(),
                    travel.getTransport() != null ? travel.getTransport().getId() : null,
                    travel.getAccommodation() != null ? travel.getAccommodation().getId() : null,
                    null
            );
            redirectAttributes.addFlashAttribute("successMessage", "Viagem nacional atualizada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar viagem nacional: " + e.getMessage());
        }
        return "redirect:/travels";
    }

    @PostMapping("/international/update/{id}")
    public String updateInternationalTravel(
            @PathVariable Long id,
            @ModelAttribute InternationalTravel travel,
            RedirectAttributes redirectAttributes) {

        try {
            travelService.updateTravel(
                    id,
                    Travel.TravelType.INTERNATIONAL,
                    travel.getDestination(),
                    travel.getDepartureDate(),
                    travel.getReturnDate(),
                    travel.getPrice(),
                    travel.getClient().getId(),
                    travel.getTransport() != null ? travel.getTransport().getId() : null,
                    travel.getAccommodation() != null ? travel.getAccommodation().getId() : null,
                    travel.isNeedsVisa()
            );
            redirectAttributes.addFlashAttribute("successMessage", "Viagem internacional atualizada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar viagem internacional: " + e.getMessage());
        }
        return "redirect:/travels";
    }

    @PostMapping("/delete/{id}")
    public String deleteTravel(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            travelService.deleteTravel(id);
            redirectAttributes.addFlashAttribute("successMessage", "Viagem removida com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao remover viagem: " + e.getMessage());
        }
        return "redirect:/travels";
    }

    @GetMapping("/national")
    public String listNationalTravels(Model model) {
        model.addAttribute("travels", travelService.getAllNationalTravels());
        model.addAttribute("travelType", "Nacionais");
        return "travels/list";
    }

    @GetMapping("/international")
    public String listInternationalTravels(Model model) {
        model.addAttribute("travels", travelService.getAllInternationalTravels());
        model.addAttribute("travelType", "Internacionais");
        return "travels/list";
    }

    private void prepareTravelFormModel(Model model, String type) {
        model.addAttribute("clients", clientService.getAllClients());
        model.addAttribute("transports", transportService.getAllTransports());
        model.addAttribute("accommodations", accommodationService.getAllAccommodations());
        model.addAttribute("travelType", type);
        model.addAttribute("today", LocalDate.now());
    }
}