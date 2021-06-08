package com.whizchua.lil.designpatternsapp.controller;

import com.whizchua.lil.designpatternsapp.builder.Contact;
import com.whizchua.lil.designpatternsapp.builder.ContactBuilder;
import com.whizchua.lil.designpatternsapp.factory.Child;
import com.whizchua.lil.designpatternsapp.factory.ChildFactory;
import com.whizchua.lil.designpatternsapp.repository.PresidentEntity;
import com.whizchua.lil.designpatternsapp.repository.PresidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class AppController {

    @Autowired
    private ChildFactory ChildFactory;

    @GetMapping
    public String getDefault(){
        return "{\"message\": \"I am working\"}";
    }

    @PostMapping("adoptadoptChild/{type}/{name}")
    public Child adoptadoptChild(@PathVariable String type, @PathVariable String name){
        Child Child1 = this.ChildFactory.createChild(type);
        Child1.setName(name);
        Child1.feed();
        return Child1;
    }

    @GetMapping("parents")
    public List<Contact> getparents(){
        List<Contact> contacts = new ArrayList<>();

        Contact contact = new Contact();
        //contact.setFirstName("George");
        //contact.setLastName("Washington");
        //contacts.add(contact);

        //contacts.add(new Contact("John", "Adams", null));

        //contacts.add(new ContactBuilder().setFirstName("Thomas").setLastName("Jefferson").buildContact());

        return contacts;
    }

    @Autowired
    PresidentRepository presidentRepository;

    @GetMapping("parents/{id}")
    public PresidentEntity getPresById(@PathVariable Long id){
        return this.presidentRepository.findById(id).get();
    }

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("presidentContact/{id}")
    public Contact getPresContById(@PathVariable Long id){
        PresidentEntity entity = this.restTemplate
                .getForEntity("http://localhost:8080/parents/{id}",
                        PresidentEntity.class,
                        id).getBody();
        return (new ContactBuilder()
            .setFirstName(entity.getFirstName())
        .setLastName(entity.getLastName())
        .setEmailAddress(entity.getEmailAddress())
        .buildContact());
    }

}
