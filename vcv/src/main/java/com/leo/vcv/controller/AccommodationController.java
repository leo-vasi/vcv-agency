package com.leo.vcv.controller;

import com.leo.vcv.model.Accommodation;
import com.leo.vcv.service.AccommodationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/accommodations")
public class AccommodationController {

    private final AccommodationService accommodationService;

    public AccommodationController(AccommodationService accommodationService) {
        this.accommodationService = accommodationService;
    }

    @GetMapping
    public String listAccommodations(Model model) {
        model.addAttribute("accommodations", accommodationService.getAllAccommodations());
        return "accommodations/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("accommodation", new Accommodation());
        return "accommodations/create";
    }

    @PostMapping
    public String createAccommodation(@ModelAttribute Accommodation accommodation,
                                      RedirectAttributes redirectAttributes) {
        try {
            accommodationService.createAccommodation(
                    accommodation.getHotelName(),
                    accommodation.getAddress(),
                    accommodation.getCategory(),
                    accommodation.getDailyRate(),
                    accommodation.getRoomCount(),
                    accommodation.getHasBreakfast(),
                    accommodation.getHasPool(),
                    accommodation.getHasWifi(),
                    accommodation.getNotes()
            );
            redirectAttributes.addFlashAttribute("successMessage", "Acomodação criada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao criar acomodação: " + e.getMessage());
        }
        return "redirect:/accommodations";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("accommodation", accommodationService.getAccommodationById(id));
            return "accommodations/edit";
        } catch (EntityNotFoundException e) {
            return "redirect:/accommodations";
        }
    }

    @PostMapping("/update/{id}")
    public String updateAccommodation(@PathVariable Long id,
                                      @ModelAttribute Accommodation accommodation,
                                      RedirectAttributes redirectAttributes) {
        try {
            accommodationService.updateAccommodation(id, accommodation);
            redirectAttributes.addFlashAttribute("successMessage", "Acomodação atualizada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar acomodação: " + e.getMessage());
        }
        return "redirect:/accommodations";
    }

    @PostMapping("/delete/{id}")
    public String deleteAccommodation(@PathVariable Long id,
                                      RedirectAttributes redirectAttributes) {
        try {
            accommodationService.deleteAccommodation(id);
            redirectAttributes.addFlashAttribute("successMessage", "Acomodação removida com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao remover acomodação: " + e.getMessage());
        }
        return "redirect:/accommodations";
    }

    @GetMapping("/luxury")
    public String listLuxuryAccommodations(Model model) {
        model.addAttribute("accommodations", accommodationService.findLuxuryAccommodations());
        return "accommodations/list";
    }
}