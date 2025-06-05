package com.leo.vcv.controller;

import com.leo.vcv.model.Transport;
import com.leo.vcv.service.TransportService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/transports")
public class TransportController {

    private final TransportService transportService;

    public TransportController(TransportService transportService) {
        this.transportService = transportService;
    }

    @GetMapping
    public String listTransports(Model model) {
        model.addAttribute("transports", transportService.getAllTransports());
        return "transports/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("transport", new Transport());
        model.addAttribute("transportTypes", Transport.TransportType.values());
        model.addAttribute("classTypes", Transport.ClassType.values());
        return "transports/create";
    }

    @PostMapping
    public String createTransport(@ModelAttribute Transport transport,
                                  RedirectAttributes redirectAttributes) {
        try {
            transportService.createTransport(
                    transport.getType(),
                    transport.getCompany(),
                    transport.getNumber(),
                    transport.getClassType()
            );
            redirectAttributes.addFlashAttribute("successMessage", "Transporte criado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao criar transporte: " + e.getMessage());
        }
        return "redirect:/transports";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("transport", transportService.getTransportById(id));
            model.addAttribute("transportTypes", Transport.TransportType.values());
            model.addAttribute("classTypes", Transport.ClassType.values());
            return "transports/edit";
        } catch (EntityNotFoundException e) {
            return "redirect:/transports";
        }
    }

    @PostMapping("/update/{id}")
    public String updateTransport(@PathVariable Long id,
                                  @ModelAttribute Transport transport,
                                  RedirectAttributes redirectAttributes) {
        try {
            transportService.updateTransport(
                    id,
                    transport.getCompany(),
                    transport.getNumber(),
                    transport.getClassType()
            );
            redirectAttributes.addFlashAttribute("successMessage", "Transporte atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar transporte: " + e.getMessage());
        }
        return "redirect:/transports";
    }

    @PostMapping("/delete/{id}")
    public String deleteTransport(@PathVariable Long id,
                                  RedirectAttributes redirectAttributes) {
        try {
            transportService.deleteTransport(id);
            redirectAttributes.addFlashAttribute("successMessage", "Transporte removido com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao remover transporte: " + e.getMessage());
        }
        return "redirect:/transports";
    }

    @GetMapping("/by-type/{type}")
    public String listTransportsByType(@PathVariable Transport.TransportType type, Model model) {
        model.addAttribute("transports", transportService.getTransportsByType(type));
        return "transports/list";
    }
}