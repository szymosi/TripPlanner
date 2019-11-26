package com.szymonosicinski.tripplanner.Util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.szymonosicinski.tripplanner.Entity.User;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class UserPrincipal implements UserDetails {

    @ApiModelProperty(hidden = true)
    private UUID id;

    @ApiModelProperty(hidden = true)
    private String name;

    @ApiModelProperty(hidden = true)
    private String surname;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private String password;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private String username;

    @ApiModelProperty(hidden = true)
    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(final UUID id, String name, String surname, String password, String username,
                         final Collection<? extends GrantedAuthority> authorities){
        this.id=id;
        this.name=name;
        this.surname=surname;
        this.password=password;
        this.username=username;
        this.authorities=authorities;
    }

    public static UserPrincipal create(final User user){
        final List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("User"));
        return new UserPrincipal(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getPassword(),
                user.getUsername(),
                authorities);
    }

    public UUID getId(){return id;}
    public String getName() {return name;}
    public String getSurname() {return surname;}

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @ApiModelProperty(hidden = true)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @ApiModelProperty(hidden = true)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @ApiModelProperty(hidden = true)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @ApiModelProperty(hidden = true)
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        final UserPrincipal that = (UserPrincipal) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
