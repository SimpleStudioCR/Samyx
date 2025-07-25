package snn.soluciones.com.controllers;

import snn.soluciones.com.models.entity.CBarrio;
import snn.soluciones.com.models.entity.CCanton;
import snn.soluciones.com.models.entity.CDistrito;
import snn.soluciones.com.models.entity.CProvincia;
import snn.soluciones.com.service.interfaces.ICBarrioService;
import snn.soluciones.com.service.interfaces.ICCantonService;
import snn.soluciones.com.service.interfaces.ICDistritoService;
import snn.soluciones.com.service.interfaces.ICProvinciaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping({"/ubicacion"})
public class UbicacionController {
  @Autowired
  private ICProvinciaService _provinciaService;
  
  @Autowired
  private ICCantonService _cantonService;
  
  @Autowired
  private ICDistritoService _distritoService;
  
  @Autowired
  private ICBarrioService _barrioService;
  
  @PostMapping({"/provincias"})
  public String getProvincias(Model model) {
    List<CProvincia> listaProvincias = this._provinciaService.findAll();
    model.addAttribute("listaProvincias", listaProvincias);
    return "ubicacion/provincias";
  }
  
  @PostMapping({"/cantones"})
  public String getCantones(Model model, @RequestParam(name = "id", defaultValue = "0") Long id) {
    List<CCanton> listaCantones = this._cantonService.findAllByIdProvincia(id);
    model.addAttribute("listaCantones", listaCantones);
    return "ubicacion/cantones";
  }
  
  @PostMapping({"/distritos"})
  public String getDistritos(Model model, @RequestParam(name = "id", defaultValue = "0") Long id) {
    List<CDistrito> listaDistritos = this._distritoService.findAllByIdCanton(id);
    model.addAttribute("listaDistritos", listaDistritos);
    return "ubicacion/distritos";
  }
  
  @PostMapping({"/barrios"})
  public String getBarrios(Model model, @RequestParam(name = "id", defaultValue = "0") Long id) {
    List<CBarrio> listaBarrios = this._barrioService.findAllByIdDistrito(id);
    model.addAttribute("listaBarrios", listaBarrios);
    return "ubicacion/barrios";
  }
}
