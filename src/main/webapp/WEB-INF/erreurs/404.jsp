<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Page introuvable - Location Maisons</title>
    <style>
        body { font-family: Arial, sans-serif; text-align: center; padding: 80px 20px; background: #f1f5f9; }
        h1 { font-size: 80px; color: #3b82f6; margin: 0; }
        h2 { font-size: 24px; color: #0f172a; margin: 20px 0; }
        p { color: #64748b; margin-bottom: 30px; }
        a { display: inline-block; padding: 12px 24px; background: #3b82f6; color: #fff; text-decoration: none; border-radius: 10px; font-weight: 600; }
    </style>
</head>
<body>
    <h1>404</h1>
    <h2>Page introuvable</h2>
    <p>La page que vous cherchez n'existe pas ou a été déplacée.</p>
    <a href="${pageContext.request.contextPath}/">Retour à l'accueil</a>
</body>
</html>