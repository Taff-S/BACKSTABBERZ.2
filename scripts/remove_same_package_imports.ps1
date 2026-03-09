$root = "src\main\java"
$files = Get-ChildItem -Path $root -Recurse -Filter *.java
$modified = @()
foreach ($file in $files) {
  $text = Get-Content -Raw -Path $file.FullName -Encoding UTF8
  $pkgMatch = [regex]::Match($text, '^[\t ]*package\s+([\w\.]+);', 'Multiline')
  if (-not $pkgMatch.Success) { continue }
  $pkg = [regex]::Escape($pkgMatch.Groups[1].Value)
  $pattern = "^[\t ]*import\s+$pkg\.[\w\.]*;[ \t]*$"
  $newText = [regex]::Replace($text, $pattern, '', 'Multiline')
  if ($newText -ne $text) {
    # collapse multiple blank lines
    $newText = [regex]::Replace($newText, "\n{3,}", "\n\n")
    Set-Content -Path $file.FullName -Value $newText -Encoding UTF8
    $modified += $file.FullName
  }
}
if ($modified.Count -gt 0) {
  Write-Output "Modified files:`n$($modified -join "`n")"
} else {
  Write-Output "No same-package imports found."
}
