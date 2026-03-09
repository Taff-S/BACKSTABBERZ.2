import re
import pathlib

ROOT = pathlib.Path('src/main/java')
changed = []
for path in ROOT.rglob('*.java'):
    text = path.read_text(encoding='utf-8')
    m = re.search(r'^\s*package\s+([\w\.]+);', text, flags=re.MULTILINE)
    if not m:
        continue
    pkg = m.group(1)
    pattern = re.compile(r'^[ \t]*import\s+' + re.escape(pkg) + r'\.[\w\.]*;\s*$', flags=re.MULTILINE)
    new_text, n = pattern.subn('', text)
    if n > 0:
        # remove any consecutive blank lines created
        new_text = re.sub(r'\n{3,}', '\n\n', new_text)
        path.write_text(new_text, encoding='utf-8')
        changed.append((str(path), n))

if changed:
    print('Modified files:')
    for p, n in changed:
        print(f'  {p}: removed {n} import(s)')
else:
    print('No same-package imports found.')
