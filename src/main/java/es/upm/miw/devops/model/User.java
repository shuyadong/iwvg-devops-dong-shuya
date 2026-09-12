package es.upm.miw.devops.model;

public record User(String id, String firstName, String familyName, String email, String identity,
                    String address, String city, String province, String postalCode,
                    boolean active, Role role) {

    public boolean billable() {
        return hasContent(this.firstName) && hasContent(this.familyName) && hasContent(this.email)
                && hasContent(this.identity) && hasContent(this.address) && hasContent(this.city)
                && hasContent(this.province) && hasContent(this.postalCode);
    }

    private static boolean hasContent(String value) {
        return value != null && !value.isBlank();
    }
}
