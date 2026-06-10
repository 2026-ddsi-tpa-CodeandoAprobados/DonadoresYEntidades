package ar.edu.utn.dds.k3003;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.*;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonadoresYEntidades;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaIncentivos;
import ar.edu.utn.dds.k3003.exceptions.*;
import ar.edu.utn.dds.k3003.repositories.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.transaction.Transactional;
import lombok.val;
import java.util.List;
import java.util.NoSuchElementException;

public class Fachada implements FachadaDonadoresYEntidades {
    private FachadaIncentivos fachadaIncentivos;

    //MAPPERS
    private DonadoresYEntidadesDataMapper donadoresYEntidadesDataMapper = new DonadoresYEntidadesDataMapper();
    private NecesidadMaterialDataMapper necesidadMaterialDataMapper = new NecesidadMaterialDataMapper();
    private QuejaDataMapper quejaDataMapper = new QuejaDataMapper();

    //DATABASE
    private DonadoresRepository donadoresRepository;
    private EntidadesBeneficasRepository entidadesBeneficasRepository;
    private NecesidadMaterialRepository necesidadMaterialRepository;
    private QuejasRepository quejasRepository;

    public Fachada(EntityManager entityManager, EntityTransaction transaction) {
        this.donadoresRepository = new InDataBaseDonadoresRepo(entityManager,transaction);
        this.entidadesBeneficasRepository = new InDataBaseEntidadesBeneficasRepo(entityManager,transaction);
        this.necesidadMaterialRepository = new InDataBaseNecesidadMaterialRepo(entityManager,transaction);
        this.quejasRepository = new InDataBaseQuejasRepo(entityManager,transaction);
    }
    public Fachada(){
        this.donadoresRepository = new InMemoryDonadoresRepo();
        this.entidadesBeneficasRepository = new InMemoryEntidadesBeneficasRepo();
        this.necesidadMaterialRepository = new InMemoryNecesidadMaterialRepo();
        this.quejasRepository = new InMemoryQuejasRepo();
    }

    public List<EntidadBeneficaDTO> obtenerTodasLasEntidades(){
        return entidadesBeneficasRepository.todasLasEntidades().stream().map(x -> donadoresYEntidadesDataMapper.toEntidadBeneficaDTO(x)).toList();
    }

    public List<DonadorDTO> obtenerTodosLosDonadores() {
        return donadoresRepository.todosLosDonadores().stream().map(x -> donadoresYEntidadesDataMapper.toDonadorDTO(x)).toList();
    }

    //ALTA DONADOR
    @Override
    public DonadorDTO agregarDonador(DonadorDTO donadorDTO) {
        if (this.donadoresRepository.findById(donadorDTO.id()).isPresent()) {
            throw new DonadorYaExistenteException("Ya existe un donador con ese ID");
        }
        val donador = donadoresYEntidadesDataMapper.toDonador(donadorDTO);
        val donadorGuardado = donadoresRepository.save(donador);
        return donadoresYEntidadesDataMapper.toDonadorDTO(donadorGuardado);
    }

    //BAJA DONADOR
    @Transactional
    public DonadorDTO quitarDonador(String donadorID){
        if (donadorID == null) {
            throw new RuntimeException("El donadorID no puede ser nulo");
        }
        if (donadoresRepository.findById(donadorID).isEmpty()) {
            throw new DonadorNoEncontradoException("No existe un donador con ese ID");
        }
        val donadorBorrado = this.donadoresRepository.deleteById(donadorID);
        return donadoresYEntidadesDataMapper.toDonadorDTO(donadorBorrado);
    }

    //MODIFICAR DONADOR
    @Override
    public QuejaDTO agregarQueja(QuejaDTO quejaDTO) throws NoSuchElementException{
        if (quejaDTO == null) {
            throw new RuntimeException("La queja no puede ser nula");
        }
        if (quejasRepository.findById(quejaDTO.id()).isPresent()) {
            throw new RuntimeException("La queja ya existe");
        }
        buscarDonadorPorID(quejaDTO.donadorID());

        val queja = quejaDataMapper.toQueja(quejaDTO);

        val guardada = quejasRepository.save(queja);

        if(quejasRepository.quejasDeUnDonador(queja.getDonadorID()).size() == 5){
            modificarEstado(queja.getDonadorID(), EstadoDonadorEnum.SOSPECHOSO);
        }
        if(quejasRepository.quejasDeUnDonador(queja.getDonadorID()).size() == 10){
            modificarEstado(queja.getDonadorID(), EstadoDonadorEnum.BANEADO);
        }

        return quejaDataMapper.toQuejaDTO(guardada);
    }

    //MODIFICAR DONADOR
    @Override
    public DonadorDTO modificarEstado(String donadorID, EstadoDonadorEnum estado) throws NoSuchElementException {
        if (estado == null) {
            throw new RuntimeException("El estado no puede ser nulo");
        }
        val donador = this.donadoresRepository.findById(donadorID);
        if (donador.isEmpty()) {
            throw new NoSuchElementException("No existe un donador con ese ID");
        }
        donador.get().setEstado(estado);
        this.donadoresRepository.save(donador.get());
        return donadoresYEntidadesDataMapper.toDonadorDTO(donador.get());
    }

    //MODIFICAR DONADOR
    @Override
    public DonadorDTO modifcarCategoria(String donadorID, String categoria) throws NoSuchElementException {
        if (categoria == null) {
            throw new RuntimeException("La categoria no puede ser nula");
        }
        val donador = this.donadoresRepository.findById(donadorID);
        if (donador.isEmpty()) {
            throw new NoSuchElementException("No existe un donador con ese ID");
        }
        donador.get().setCategoria(categoria);
        this.donadoresRepository.save(donador.get());
        return donadoresYEntidadesDataMapper.toDonadorDTO(donador.get());
    }

    @Override
    public DonadorDTO buscarDonadorPorID(String donadorID) throws NoSuchElementException {
        val donadorOptional = this.donadoresRepository.findById(donadorID);
        if (donadorOptional.isEmpty()) {
            throw new DonadorNoEncontradoException("Donador no encontrado");
        }
        val donadorFinal = donadorOptional.get();
        return donadoresYEntidadesDataMapper.toDonadorDTO(donadorFinal);

    }

    @Override
    public Boolean puedeDonar(String donadorID) throws NoSuchElementException {
        if (donadorID == null) {
            throw new RuntimeException("No puede donar");
        }
        if (buscarDonadorPorID(donadorID).estado() == EstadoDonadorEnum.SOSPECHOSO){
            return Math.random() <= 0.5;
        }
        return buscarDonadorPorID(donadorID).estado() == EstadoDonadorEnum.VERIFICADO;
    }

    @Override
    public List<QuejaDTO> obtenerQuejasDe(String donadorID) throws NoSuchElementException {
        if (donadorID == null){
            throw new NoSuchElementException("El parametro no puede ser nulo");
        }
        buscarDonadorPorID(donadorID);
        return quejasRepository.quejasDeUnDonador(donadorID).stream().map(x -> quejaDataMapper.toQuejaDTO(x)).toList();
    }

    //ALTA ENTIDAD
    @Override
    public EntidadBeneficaDTO agregarEntidad(EntidadBeneficaDTO entidadBeneficaDTO) {
        if (entidadBeneficaDTO == null) {
            throw new RuntimeException("La entidad no puede ser nula");
        }
        if (this.entidadesBeneficasRepository.findById(entidadBeneficaDTO.id()).isPresent()) {
            throw new DonadorYaExistenteException("Ya existe un entidad benefica con ese ID");
        }
        val entidadBenefica = donadoresYEntidadesDataMapper.toEntidadBenefica(entidadBeneficaDTO);
        val entidadGuardada = this.entidadesBeneficasRepository.save(entidadBenefica);
        return donadoresYEntidadesDataMapper.toEntidadBeneficaDTO(entidadGuardada);
    }

    //BAJA ENTIDAD
    public EntidadBeneficaDTO quitarEntidad(String entidadID){
        if (entidadID == null) {
            throw new RuntimeException("La entidadID no puede ser nula");
        }
        if(entidadesBeneficasRepository.findById(entidadID).isEmpty()){
            throw new EntidadBeneficaNoEncontradaException("No existe una entidad benefica con ese ID");
        }

        val entidadBajada = entidadesBeneficasRepository.deleteById(entidadID);

        return donadoresYEntidadesDataMapper.toEntidadBeneficaDTO(entidadBajada);
    }

    //MODIFICAR ENTIDAD
    public EntidadBeneficaDTO modificarRazonSocial(String entidadID, String nuevaRazon){
        if(entidadID == null){
            throw new RuntimeException("El parametro entidadID no puede ser nulo");
        }
        val entidad = entidadesBeneficasRepository.findById(entidadID);
        entidad.get().setRazonSocial(nuevaRazon);
        this.entidadesBeneficasRepository.save(entidad.get());
        return donadoresYEntidadesDataMapper.toEntidadBeneficaDTO(entidad.get());
    }


    @Override
    public EntidadBeneficaDTO buscarEntidadPorID(String entidadID) throws NoSuchElementException {
        val entidadOptional = this.entidadesBeneficasRepository.findById(entidadID);

        if (entidadOptional.isEmpty()) {
            throw new DonadorNoEncontradoException("Entidad no encontrada");
        }
        val entidadFinal = entidadOptional.get();

        return donadoresYEntidadesDataMapper.toEntidadBeneficaDTO(entidadFinal);
    }

    //ALTA NECESIDAD MATERIAL
    @Override
    public NecesidadMaterialDTO registrarNecesidad(NecesidadMaterialDTO necesidadMaterialDTO) {
        if (necesidadMaterialDTO == null) {
            throw new RuntimeException("La necesidad no puede ser nula");
        }
        if (necesidadMaterialRepository.findById(necesidadMaterialDTO.id()).isPresent()) {
            throw new RuntimeException("La necesidad ya existe");
        }
        buscarEntidadPorID(necesidadMaterialDTO.entidadID());
        val necesidadMaterial = necesidadMaterialDataMapper.toNecesidadMaterial(necesidadMaterialDTO);
        val necesidadMaterialGuardada = this.necesidadMaterialRepository.save(necesidadMaterial);
        return necesidadMaterialDataMapper.toNecesidadMaterialDTO(necesidadMaterialGuardada);
    }

    //BAJA NECESIDAD MATERIAL
    public NecesidadMaterialDTO quitarNecesidad(String necesidadID){
        if(necesidadID == null){
            throw new RuntimeException("La necesidadID no puede ser nula");
        }
        if (necesidadMaterialRepository.findById(necesidadID).isEmpty()) {
            throw new NoSuchElementException("La necesidad no existe");
        }
        val necesidadBorrada = necesidadMaterialRepository.deleteById(necesidadID);
        return necesidadMaterialDataMapper.toNecesidadMaterialDTO(necesidadBorrada);
    }

    public NecesidadMaterialDTO buscarNecesidad (String necesidadID){
        val necesidad = necesidadMaterialRepository.findById(necesidadID);
        if(necesidad.isEmpty()){
            throw new NoSuchElementException("La necesidad no existe");
        }
        return necesidadMaterialDataMapper.toNecesidadMaterialDTO(necesidad.get());
    }

    public List<NecesidadMaterialDTO> obtenerTodasLasNecesidades(){
        return necesidadMaterialRepository.todasNecesidades().
                stream().map(x->necesidadMaterialDataMapper.toNecesidadMaterialDTO(x)).toList();
    }

    //MODIFICA NECESIDAD MATERIAL
    @Override
    public NecesidadMaterialDTO satisfacerNecesidad(String necesidadID, Integer cantidad) throws NoSuchElementException {
        if (cantidad == null || cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero");
        }
        val necesidad = necesidadMaterialRepository.findById(necesidadID);
        if (necesidad.isEmpty()) {
            throw new NoSuchElementException("La necesidad no existe");
        }
        if (necesidad.get().getTipo().equals(TipoNecesidadMaterialEnum.RECURRENTE)) {
            if(cantidad < necesidad.get().getCantidadObjetivo()){
                throw new IllegalArgumentException("No se puede safisfacer una necesidad de forma parcial");
            }
            else {
                necesidad.get().setCantidadObjetivo(necesidad.get().getCantidadObjetivo() - cantidad);
            }
        }
        else if(necesidad.get().getTipo().equals(TipoNecesidadMaterialEnum.EXTRAORDINARIA)){
            necesidad.get().setCantidadObjetivo(necesidad.get().getCantidadObjetivo() - cantidad);
            this.necesidadMaterialRepository.deleteById(necesidadID);
            this.necesidadMaterialRepository.save(necesidad.get());
            return necesidadMaterialDataMapper.toNecesidadMaterialDTO(necesidad.get());
        }
        this.necesidadMaterialRepository.save(necesidad.get());
        return necesidadMaterialDataMapper.toNecesidadMaterialDTO(necesidad.get());
    }

    @Override
    public List<NecesidadMaterialDTO> obtenerNecesidadesInsatisfechasDe(String productoSolicitadoID) {
        return necesidadMaterialRepository
                .todasLasNecesidades(productoSolicitadoID)
                .stream().map(x-> necesidadMaterialDataMapper.toNecesidadMaterialDTO(x))
                .toList();
    }

    @Override
    public DonadorStatsDTO estadisticasDonador(String donadorID) throws NoSuchElementException {

    val donador = buscarDonadorPorID(donadorID);

    List<String> insigniasIds = List.of();
    String misionId = null;

    if (this.fachadaIncentivos != null) {
        val insignias = this.fachadaIncentivos.getInsigniasDeDonador(donadorID);
        insigniasIds = insignias.stream().map(i -> i.id()).toList();

        val mision = this.fachadaIncentivos.getMisionEnCursoDeDonador(donadorID);
        if (mision != null) {
            misionId = mision.id();
        }
    }
    return new DonadorStatsDTO(
            donador.id(),
            donador.nombre(),
            donador.apellido(),
            donador.edad(),
            donador.estado(),
            donador.categoria(),
            misionId,
            insigniasIds
    );
}
    @Override
    public void setFachadaIncentivos(FachadaIncentivos fachadaIncentivos) {
        this.fachadaIncentivos = fachadaIncentivos;
    }
}