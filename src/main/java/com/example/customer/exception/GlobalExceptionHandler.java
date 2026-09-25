package com.example.customer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.stream.Collectors;

/**
 * Gestionnaire global des exceptions de l'application.
 *
 * <p>Cette classe centralise le traitement des erreurs pour <b>tous les controllers</b>
 * de l'application. Elle évite de répéter des blocs {@code try/catch} dans chaque
 * endpoint et garantit que toutes les erreurs sont retournées dans un format
 * standardisé et cohérent (RFC 7807 - Problem Details).</p>
 *
 * <p>Le format {@link ProblemDetail} est le standard moderne de Spring pour
 * représenter les erreurs HTTP. Il produit un JSON contenant les champs
 * {@code type}, {@code title}, {@code status}, {@code detail} et éventuellement
 * des propriétés supplémentaires personnalisées.</p>
 *
 * <p>L'annotation {@link RestControllerAdvice} combine {@code @ControllerAdvice}
 * (interception globale) et {@code @ResponseBody} (sérialisation automatique
 * du retour en JSON).</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Intercepte les erreurs de validation déclenchées par {@code @Valid}
     * sur les paramètres de méthode (ex: {@code @RequestBody}).
     *
     * <p>Cette exception est levée par Spring lorsqu'un objet reçu dans une
     * requête ne respecte pas les contraintes de Bean Validation
     * ({@code @NotBlank}, {@code @Email}, {@code @Pattern}, etc.).</p>
     *
     * <p>Exemple de réponse JSON produite :</p>
     * <pre>{@code
     * {
     *   "type": "about:blank",
     *   "title": "Invalid Request Content",
     *   "status": 400,
     *   "detail": "Validation Failed",
     *   "errors": "name:Name cannot be empty,email:must be a well-formed email address"
     * }
     * }</pre>
     *
     * @param ex l'exception contenant le résultat de la validation (BindingResult)
     * @return un {@link ProblemDetail} avec le statut 400 et la liste des erreurs
     *         de validation, au format {@code champ:message} séparés par des virgules
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {

        // On construit un ProblemDetail avec le statut HTTP 400 (Bad Request)
        // et un message générique indiquant que la validation a échoué.
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Validation Failed");

        // Titre lisible par un humain, affiché dans la réponse JSON.
        problemDetail.setTitle("Invalid Request Content");

        // On parcourt toutes les erreurs de validation des champs et on les
        // formate en une chaîne "champ:message" séparée par des virgules.
        // Exemple : "name:Name cannot be empty,email:must be a valid email"
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ":" + error.getDefaultMessage())
                .collect(Collectors.joining(","));

        // On ajoute cette chaîne comme propriété personnalisée "errors" du ProblemDetail.
        problemDetail.setProperty("errors", errors);

        return problemDetail;
    }

    /**
     * Intercepte les erreurs 404 lorsqu'aucune ressource statique ou route
     * ne correspond à l'URL demandée.
     *
     * <p>Cette exception est levée par Spring MVC quand une requête ne peut
     * être mappée vers aucun endpoint ni aucune ressource statique. Elle est
     * fréquente lorsque le client appelle une URL inexistante (ex: une faute
     * de frappe dans le chemin).</p>
     *
     * <p>Exemple de réponse JSON produite :</p>
     * <pre>{@code
     * {
     *   "type": "https://api.crm.com/errors/not-found",
     *   "title": "Resource Not Found",
     *   "status": 404,
     *   "detail": "The requested resource was not found"
     * }
     * }</pre>
     *
     * @param ex l'exception levée par Spring MVC
     * @return un {@link ProblemDetail} avec le statut 404 et une URI de type
     *         pointant vers la documentation de l'erreur
     */
    @ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
    public ProblemDetail handleNotFoundException(
            org.springframework.web.servlet.resource.NoResourceFoundException ex) {

        // On construit un ProblemDetail avec le statut HTTP 404 (Not Found)
        // et un message explicite indiquant que la ressource n'existe pas.
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, "The requested resource was not found");

        // Titre court et lisible par un humain.
        problemDetail.setTitle("Resource Not Found");

        // Le champ "type" identifie de manière unique le type d'erreur via une URI.
        // C'est une bonne pratique RFC 7807 : le client peut se référer à cette URI
        // pour comprendre l'erreur (documentation, page d'aide, etc.).
        problemDetail.setType(URI.create("https://api.crm.com/errors/not-found"));

        return problemDetail;
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleHttpMessageNotReadableException(HttpMessageNotReadableException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,"The request body is malformed or not valid JSON"+ex.getMostSpecificCause().getMessage());
        problemDetail.setTitle("HTTP Message Not Readable");
        return problemDetail;
    }
}