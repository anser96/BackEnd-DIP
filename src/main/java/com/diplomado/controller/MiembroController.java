package com.diplomado.controller;

import com.diplomado.model.Miembro;
import com.diplomado.model.dto.MiembroDTO;
import com.diplomado.service.MiembroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/miembro")
public class MiembroController {
    @Autowired
    private MiembroService miembroService;

    @GetMapping
    public List<MiembroDTO> getAllMiembros(){
        return miembroService.findAllMiembros();
    }

    @GetMapping("/{id}")
    public Miembro getMiembroById(@PathVariable int id){
        return miembroService.findById(id);
    }

    @PostMapping
    public Miembro saveMiembro(@RequestBody Miembro miembro){
        return miembroService.save(miembro);
    }

    @DeleteMapping("/{id}")
    public void deleteMiembro(@PathVariable int id){
        miembroService.deleteById(id);
    }

}
