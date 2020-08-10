package com.brc.als.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brc.als.domain.Alert;
import com.brc.als.repository.AlertRepository;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * REST controller for managing {@link com.brc.als.domain.Alert}.
 */
@RestController
@RequestMapping("/api")
public class AlertController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String ENTITY_NAME = "collector";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private AlertRepository alertRepository;
    

    /**
     * {@code POST  /updateAlert} : update an entry in alert.
     *
     * @param guid: alert's guid.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body in updated alert,
     * or with status {@code 417 Exception_Failed} if guid is null,
     */
    @PostMapping("/updateAlert")
    public List<Alert> updateAlert(@RequestBody ObjectNode obj) {
    	logger.info("Request to update alert. Request object : "+obj);
        try {
        	Long id = obj.get("id").asLong();
        	Alert al = new Alert();
        	al.setId(id);
        	Optional<Alert> oa = alertRepository.findOne(Example.of(al));
            String alertState = obj.get("alertState").asText();
            if(oa.isPresent()) {
            	Alert alert = oa.get();
            	alert.setAlertstate(alertState);
            	alert = alertRepository.save(alert);
            	logger.debug("Alert updated successfully");
            }
            
        }catch(Exception e) {
        	logger.error("Error in updating alert: ",e);
        	return Collections.emptyList();
        }
        return getAllAlert();
    }
    
	@GetMapping("/listAlert")
	public List<Alert> getAllAlert() {
	    logger.debug("Request to get all alerts");
	    List<Alert> list = alertRepository.findAll(Sort.by(Direction.DESC, "id"));
	    return list;
	}
    
    /**
     * {@code POST  /addFolderToLibrary} : add an entry in library.
     *
     * @param folderId: folder's id.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the new library,
     * or with status {@code 400 (Bad Request)} if folderId is null,
     * or with status {@code 500 (Internal Server Error)} if the library couldn't be added.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
//    @PostMapping("/addFolderToLibrary")
//    public ResponseEntity<Library> addFolderToLibrary(@RequestParam Long folderId) throws URISyntaxException {
//        logger.info(String.format("Request to add a folder to library. folder id : %d", folderId));
//        
//        Optional<Folder> olf = folderRepository.findById(folderId);
//        
//        Library library = new Library();
//        library.setFolder(olf.get());
//        
//        library = libraryRepository.save(library);
//        
//        return ResponseEntity.created(new URI("/api/addFolderToLibrary/" + library.getId()))
//            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, library.getId().toString()))
//            .body(library);
//    }
//    
//    /**
//     * {@code GET  /listLibrary} : get all the library.
//     *
//     * @return the {@link List<Library>} with status {@code 200 (OK)} and the list of library in body.
//     */
//    @GetMapping("/listLibrary")
//    public List<Library> getAllLibrary() {
//        logger.debug("Request to get all library");
//        return libraryRepository.findAll(Sort.by(Direction.DESC, "id"));
//    }
//    
//    @GetMapping("/listLibraryTree")
//    public List<LibraryTree> getLibraryTree() {
//    	List<LibraryTree> parentList = new ArrayList<>();
//        logger.debug("Request to get library tree");
//        List<Library> libraryList = libraryRepository.findAll(Sort.by(Direction.DESC, "id"));
//        Map<Long, LibraryTree> orgMap = new HashMap<Long, LibraryTree>();  
//        
//        for(Library library: libraryList) {
//        	LibraryTree folderNode = null;
//        	if(!orgMap.containsKey(library.getFolder().getId())) {
//        		folderNode = new LibraryTree();
//        		folderNode.setId(library.getFolder().getId());
//        		folderNode.setParentId(library.getFolder().getParentId());
//        		folderNode.setIsFolder(true);
//        		folderNode.setHasChild(true);
//        		folderNode.setName(library.getFolder().getTitle());
//        		folderNode.setDescription("folder description to do");
//        		
//        		orgMap.put(library.getFolder().getId(), folderNode);
//        	}else {
//        		folderNode = orgMap.get(library.getFolder().getId());
//        	}
//        	Collector col = library.getCollector();
//    		LibraryTree collectorNode = new LibraryTree();
//    		collectorNode.setId(col.getId());
//    		collectorNode.setParentId(folderNode.getId());
//    		collectorNode.setName(col.getName());
//    		collectorNode.setDescription(col.getDescription());
//    		collectorNode.setIsFolder(false);
//    		collectorNode.setHasChild(false);
//    		
//    		Dashboard dashboard = new Dashboard();
//    		dashboard.setCollector(col);
//    		List<Dashboard> dsList = dashboardRepository.findAll(Example.of(dashboard));
//    		for(Dashboard d: dsList) {
//    			CatalogDetail cd = new CatalogDetail();
//    			cd.setId(d.getId());
//    			cd.setTitle(d.getName());
//    			cd.setDescription(d.getDescription());
//    			collectorNode.getDashboardList().add(cd);
//    		}
//    		folderNode.getItems().add(collectorNode);
//        }
//        
//        for(LibraryTree lt : orgMap.values()) {
//        	getTree(lt, parentList);
//        }
//        LibraryTree finalTree = new LibraryTree();
//        finalTree.setName("Library");
//        finalTree.setIsFolder(true);
//        for(LibraryTree lt: parentList) {
//        	finalTree.getItems().add(lt);
//        }
//        List<LibraryTree> finalList = new ArrayList<>();
//        finalList.add(finalTree);
//        return finalList;
//    }
//    
//    private void getTree(LibraryTree libraryTree, List<LibraryTree> finalTree) {
//    	
//    	if(!Objects.isNull(libraryTree.getParentId())) {
//    		Folder parentFolder = folderRepository.findById(libraryTree.getParentId()).get();
//    		LibraryTree parentNode = new LibraryTree();
//    		
//    		parentNode.setId(parentFolder.getId());
//    		parentNode.setParentId(parentFolder.getParentId());
//    		parentNode.setIsFolder(true);
//    		parentNode.setHasChild(true);
//    		parentNode.setName(parentFolder.getTitle());
//    		parentNode.setDescription("folder description to do");
//    		
//    		parentNode.getItems().add(libraryTree);
//    		getTree(parentNode, finalTree);
//    		
//    	}else {
//    		finalTree.add(libraryTree);
//    	}
//    }

    
}
