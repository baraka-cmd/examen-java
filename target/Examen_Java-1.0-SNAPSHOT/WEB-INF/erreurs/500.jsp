<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Erreur serveur - Location Maisons</title>
    <style>
        body { font-family: Arial, sans-serif; text-align: center; padding: 80px 20px; background: #f1f5f9; }
        h1 { font-size: 80px; color: #ef4444; margin: 0; }
        h2 { font-size: 24px; color: #0f172a; margin: 20px 0; }
        p { color: #64748b; margin-bottom: 30px; }
        a { display: inline-block; padding: 12px 24px; background: #3b82f6; color: #fff; text-decoration: none; border-radius: 10px; font-weight: 600; }
        .details { font-size: 12px; color: #94a3b8; margin-top: 30px; max-width: 600px; margin-left: auto; margin-right: auto; word-break: break-all; }
    </style>
</head>
<body>
    <h1>500</h1>
    <h2>Erreur interne du serveur</h2>
    <p>Une erreur inattendue s'est produite. Veuillez réessayer plus tard.</p>
    <a href="${pageContext.request.contextPath}/dashboard">Retour au dashboard</a>
    <br><br>
    <a href="${pageContext.request.contextPath}/connexion">Retour à la connexion</a>
    
    <%-- Afficher l'erreur seulement en développement --%>
    <% if (exception != null) { %>
        <div class="details">
            <strong>Détail :</strong> <%= exception.getMessage() %>
        </div>
    <% } %>
</body>
</html>