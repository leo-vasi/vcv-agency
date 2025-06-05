package com.leo.vcv.controller;

import com.leo.vcv.model.Client;
import com.leo.vcv.service.ClientService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public String listClients(Model model) {
        model.addAttribute("clients", clientService.getAllClients());
        return "clients/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("client", new Client());
        return "clients/create";
    }

    @PostMapping
    public String createClient(@ModelAttribute Client client, RedirectAttributes redirectAttributes) {
        try {
            clientService.createClient(client.getName(), client.getEmail(), client.getPhone(), client.getPassportNumber());
            redirectAttributes.addFlashAttribute("successMessage", "Cliente criado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao criar cliente: " + e.getMessage());
        }
        return "redirect:/clients";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("client", clientService.getClientById(id));
            return "clients/edit";
        } catch (EntityNotFoundException e) {
            return "redirect:/clients";
        }
    }

    @PostMapping("/update/{id}")
    public String updateClient(@PathVariable Long id, @ModelAttribute Client client, RedirectAttributes redirectAttributes) {
        try {
            clientService.updateClient(id, client.getName(), client.getPhone(), client.getPassportNumber());
            redirectAttributes.addFlashAttribute("successMessage", "Cliente atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar cliente: " + e.getMessage());
        }
        return "redirect:/clients";
    }

    @PostMapping("/delete/{id}")
    public String deleteClient(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            clientService.deleteClient(id);
            redirectAttributes.addFlashAttribute("successMessage", "Cliente removido com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao remover cliente: " + e.getMessage());
        }
        return "redirect:/clients";
    }
}