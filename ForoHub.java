
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import javax.persistence.*;
import java.util.List;

@SpringBootApplication
public class ForoHubApplication {
    public static void main(String[] args) {
        SpringApplication.run(ForoHubApplication.class, args);
    }
}

@Entity
class Topico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String mensaje;
    private String fechaCreacion;
    private String status;
    private String autor;

    @ManyToOne
    private Curso curso;

    // Getters and Setters
}

@Entity
class Respuesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mensaje;
    private String fechaCreacion;
    private String autor;
    private boolean solucion;

    @ManyToOne
    private Topico topico;

    // Getters and Setters
}

@Entity
class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String categoria;

    // Getters and Setters
}

@Entity
class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String correoElectronico;
    private String contrasena;

    @ManyToMany
    private List<Perfil> perfiles;

    // Getters and Setters
}

@Entity
class Perfil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;

    // Getters and Setters
}

interface TopicoRepository extends JpaRepository<Topico, Long> {
    // Define custom queries if necessary
}

@Service
class TopicoService {
    @Autowired
    private TopicoRepository topicoRepository;

    public Topico saveTopico(Topico topico) {
        return topicoRepository.save(topico);
    }

    public List<Topico> findAllTopicos() {
        return topicoRepository.findAll();
    }

    public Topico findTopicoById(Long id) {
        return topicoRepository.findById(id).orElse(null);
    }

    public Topico updateTopico(Long id, Topico newTopico) {
        return topicoRepository.findById(id)
            .map(topico -> {
                topico.setTitulo(newTopico.getTitulo());
                topico.setMensaje(newTopico.getMensaje());
                return topicoRepository.save(topico);
            }).orElseGet(() -> {
                newTopico.setId(id);
                return topicoRepository.save(newTopico);
            });
    }

    public void deleteTopico(Long id) {
        topicoRepository.deleteById(id);
    }
}

@RestController
@RequestMapping("/topicos")
class TopicoController {
    @Autowired
    private TopicoService topicoService;

    @PostMapping
    public Topico createTopico(@RequestBody Topico topico) {
        return topicoService.saveTopico(topico);
    }

    @GetMapping
    public List<Topico> getAllTopicos() {
        return topicoService.findAllTopicos();
    }

    @GetMapping("/{id}")
    public Topico getTopicoById(@PathVariable Long id) {
        return topicoService.findTopicoById(id);
    }

    @PutMapping("/{id}")
    public Topico updateTopico(@PathVariable Long id, @RequestBody Topico topico) {
        return topicoService.updateTopico(id, topico);
    }

    @DeleteMapping("/{id}")
    public void deleteTopico(@PathVariable Long id) {
        topicoService.deleteTopico(id);
    }
}
